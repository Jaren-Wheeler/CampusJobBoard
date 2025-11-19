package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.CampusJobBoard.dto.AdminSummaryResponse;
import com.example.CampusJobBoard.dto.UpdateSuperAdminRequest;


import java.util.List;
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
     * Performs business-rule validation such as admin limit and duplicate email.
     */
    public void createAdmin(CreateAdminRequest req) {

        // --- BUSINESS RULE CHECKS --------------------------------------

        // Limit: Only 3 admins allowed
        if (countAdmins() >= 3) {
            throw new IllegalStateException("Cannot exceed 3 admin accounts.");
        }

        // Duplicate email check
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("Email is already in use.");
        }

        // --- CREATE NEW ADMIN USER -------------------------------------

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
        List<User> admins = userRepository.findByRole(User.Role.ADMIN);

        return admins.stream()
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
     * Updates the currently logged in SUPER_ADMIN's profile.
     * Allows changing: fullName, email, and password.
     *
     * @param currentEmail The email of the logged-in super admin (from Authentication)
     * @param request DTO containing updated profile fields
     */
    public void updateSuperAdminProfile(String currentEmail, UpdateSuperAdminRequest request) {

        // Find the current super admin user
        User superAdmin = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalStateException("Super Admin not found."));

        // Handle email change — prevent duplicates
        if (!superAdmin.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalStateException("Email is already in use.");
            }
            superAdmin.setEmail(request.getEmail());
        }

        // Update name
        superAdmin.setFullName(request.getFullName());

        // 4. Update password only if the user typed a new one
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            superAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Save changes
        userRepository.save(superAdmin);
    }
}
