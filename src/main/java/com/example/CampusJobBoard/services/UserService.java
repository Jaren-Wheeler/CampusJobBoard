package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.dto.AdminSummaryResponse;
import com.example.CampusJobBoard.dto.SuperAdminProfileResponse;
import com.example.CampusJobBoard.dto.UpdateSuperAdminRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import com.example.CampusJobBoard.exceptions.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new ADMIN account.
     */
    public void createAdmin(CreateAdminRequest req) {

        if (countAdmins() >= 3) {
            throw new IllegalStateException("Cannot exceed 3 admin accounts.");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("Email is already in use.");
        }

        User admin = new User();
        admin.setFullName(req.getFullName());
        admin.setEmail(req.getEmail());
        admin.setPassword(passwordEncoder.encode(req.getPassword()));
        admin.setRole(User.Role.ADMIN);

        userRepository.save(admin);
    }

    /**
     * Returns a list of all admin accounts.
     */
    public List<AdminSummaryResponse> getAllAdmins() {
        return userRepository.findByRole(User.Role.ADMIN)
                .stream()
                .map(user -> new AdminSummaryResponse(
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of ADMIN accounts.
     */
    public long countAdmins() {
        return userRepository.countByRole(User.Role.ADMIN);
    }

    /**
     * Deletes an admin user only if they are actually an ADMIN.
     */
    public void deleteAdminById(Long userId) {

        boolean isAdmin = userRepository.existsByUserIdAndRole(userId, User.Role.ADMIN);

        if (!isAdmin) {
            throw new IllegalArgumentException("User is not an ADMIN or does not exist.");
        }

        userRepository.deleteById(userId);
    }

    /**
     * Returns the current super admin’s profile information.
     * Used for pre-filling the setup page.
     */
    public SuperAdminProfileResponse getSuperAdminProfile(String email) {
        User superAdmin = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Super Admin not found."));

        return new SuperAdminProfileResponse(
                superAdmin.getFullName(),
                superAdmin.getEmail()
        );
    }


    /**
     * Updates the currently logged in SUPER_ADMIN's profile.
     */
    public void updateSuperAdminProfile(String currentEmail, UpdateSuperAdminRequest request) {

        User superAdmin = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalStateException("Super Admin not found."));

        Map<String, String> errors = new HashMap<>();

        // ----------------------------
        // FULL NAME VALIDATION
        // ----------------------------
        String fullName = request.getFullName();
        if (fullName == null || fullName.isBlank()) {
            errors.put("fullName", "Full name is required.");
        } else {
            if (!fullName.matches("^[A-Za-z]+( [A-Za-z]+)+$")) {
                errors.put("fullName", "Enter a valid full name (first and last).");
            }
            if (fullName.length() < 3 || fullName.length() > 50) {
                errors.put("fullName", "Full name must be between 3 and 50 characters.");
            }
        }

        // ----------------------------
        // EMAIL VALIDATION
        // ----------------------------
        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            errors.put("email", "Email is required.");
        } else {
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Enter a valid email address.");
            } else if (email.equals(superAdmin.getEmail())) {
                errors.put("email", "You must provide a new email address.");
            } else if (userRepository.existsByEmail(email)) {
                errors.put("email", "This email is already in use.");
            }
        }

        // ----------------------------
        // PASSWORD VALIDATION
        // ----------------------------
        String password = request.getPassword();
        if (password == null || password.isBlank()) {
            errors.put("password", "A new password is required.");
        } else {
            if (password.length() < 8) {
                errors.put("password", "Password must be at least 8 characters.");
            }
            if (!password.matches(".*[A-Z].*")) {
                errors.put("password", "Password must contain an uppercase letter.");
            }
            if (!password.matches(".*[a-z].*")) {
                errors.put("password", "Password must contain a lowercase letter.");
            }
            if (!password.matches(".*\\d.*")) {
                errors.put("password", "Password must contain a number.");
            }
            if (!password.matches(".*[!@#$%^&*()].*")) {
                errors.put("password", "Password must contain a special character.");
            }
        }

        // If any validation failed, return errors
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Apply updates
        superAdmin.setFullName(fullName);
        superAdmin.setEmail(email);
        superAdmin.setPassword(passwordEncoder.encode(password));

        // Mark setup as completed
        superAdmin.setMustUpdateProfile(false);

        userRepository.save(superAdmin);
    }

}
