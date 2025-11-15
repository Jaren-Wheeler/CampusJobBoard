package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAdmin(CreateAdminRequest req) {
        // Prevent duplicate admin accounts
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create admin user
        User admin = new User();
        admin.setFullName(req.getFullName());
        admin.setEmail(req.getEmail());
        admin.setPassword(passwordEncoder.encode(req.getPassword()));
        admin.setRole(User.Role.ADMIN);

        userRepository.save(admin);
    }
}
