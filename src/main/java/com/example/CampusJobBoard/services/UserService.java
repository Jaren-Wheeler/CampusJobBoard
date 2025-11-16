package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.CampusJobBoard.dto.AdminSummaryResponse;

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
     * Validates email and password.
     */
    public void createAdmin(CreateAdminRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
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
}
