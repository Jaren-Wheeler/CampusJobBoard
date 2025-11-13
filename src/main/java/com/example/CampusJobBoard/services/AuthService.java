package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.AuthResponse;
import com.example.CampusJobBoard.dto.LoginRequest;
import com.example.CampusJobBoard.dto.RegisterRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService handles the main authentication logic for the system.
 * Responsibilities:
 * - Register new users with encrypted passwords
 * - Authenticate existing users by verifying credentials
 * - Return simple success responses for now
 * This service interacts directly with the UserRepository and
 * applies basic validation before creating or validating accounts.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user if the email is not already taken.
     * Prevents self-registration as an admin for security reasons.
     *
     * @param request contains user details from the registration form
     * @return a simple AuthResponse confirming successful registration
     */
    public AuthResponse register(RegisterRequest request) {
        // Prevent duplicate accounts
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Prevent users from assigning themselves admin privileges
        if (request.getRole().toString().equals("ADMIN")) {
            throw new RuntimeException("Cannot self-register as admin");
        }

        // Create a new user and encode their password before saving
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);

        return new AuthResponse("Registered successfully", user.getRole().toString());
    }

    /**
     * Authenticates an existing user by verifying email and password.
     *
     * @param request contains login credentials (email + password)
     * @return an AuthResponse indicating login success and role
     */
    public AuthResponse login(LoginRequest request) {
        // Look up user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password match
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse("Login successful", user.getRole().toString());
    }
}
