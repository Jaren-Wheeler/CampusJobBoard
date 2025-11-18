package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.dto.AuthResponse;
import com.example.CampusJobBoard.dto.LoginRequest;
import com.example.CampusJobBoard.dto.RegisterRequest;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import com.example.CampusJobBoard.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration and login logic.
 * Issues JWT tokens for authenticated sessions.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user (STUDENT or EMPLOYER) and returns a signed JWT token.
     * Admin accounts cannot self-register.
     */
    public AuthResponse register(RegisterRequest request) {

        // Duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already registered.");
        }

        if (request.getRole() == null) {
            throw new IllegalStateException("Role is required.");
        }

        // Prevent self-registering as admin
        if (request.getRole() == User.Role.ADMIN) {

            if (userRepository.countByRole(User.Role.ADMIN) >= 3) {
                throw new IllegalStateException("Maximum of 3 admin accounts allowed.");
            }

            throw new IllegalStateException("Cannot self-register as admin.");
        }

        // Create user
        User newUser = new User();
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());

        userRepository.save(newUser);

        // Build token
        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername(newUser.getEmail())
                .password(newUser.getPassword())
                .roles(newUser.getRole().name())
                .build();

        String jwt = jwtService.generateToken(details);

        return new AuthResponse(jwt, newUser.getRole().name());
    }

    /**
     * Authenticates an existing user and returns a signed JWT token.
     */
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String jwt = jwtService.generateToken(details);

        return new AuthResponse(jwt, user.getRole().name());
    }
}
