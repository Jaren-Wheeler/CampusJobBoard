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
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (request.getRole().toString().equals("ADMIN")) {
            throw new RuntimeException("Cannot self-register as admin");
        }

        User newUser = new User();
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        userRepository.save(newUser);

        // Build UserDetails for token generation
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(newUser.getEmail())
                .password(newUser.getPassword())
                .roles(newUser.getRole().name())
                .build();

        // Generate JWT after registration
        String jwtToken = jwtService.generateToken(userDetails);

        // Return the token and role
        return new AuthResponse(jwtToken, newUser.getRole().toString());
    }

    /**
     * Authenticates an existing user and returns a signed JWT token.
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Build UserDetails for token generation
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        // Generate JWT for login
        String jwtToken = jwtService.generateToken(userDetails);

        // Return the token and role
        return new AuthResponse(jwtToken, user.getRole().toString());
    }
}
