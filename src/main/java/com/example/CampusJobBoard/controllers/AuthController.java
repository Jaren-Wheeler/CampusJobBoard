package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.dto.LoginRequest;
import com.example.CampusJobBoard.dto.RegisterRequest;
import com.example.CampusJobBoard.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;


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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }


    /**
     * Logs in an existing user.
     * Accessible via POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        }
    }

}
