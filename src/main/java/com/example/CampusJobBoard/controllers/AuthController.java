package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.dto.AuthResponse;
import com.example.CampusJobBoard.dto.LoginRequest;
import com.example.CampusJobBoard.dto.RegisterRequest;
import com.example.CampusJobBoard.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication-related API requests.
 * Provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Injects the AuthService for handling authentication logic
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     * Accessible via POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Logs in an existing user.
     * Accessible via POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
