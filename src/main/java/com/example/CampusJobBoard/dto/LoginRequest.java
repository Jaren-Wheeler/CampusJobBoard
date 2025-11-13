package com.example.CampusJobBoard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest is a Data Transfer Object that captures user credentials during login.
 * Includes:
 * - Email
 * - Password
 * Validation annotations help ensure data integrity
 * before the request reaches the authentication service.
 */
public class LoginRequest {

    /** User’s email address, validated for proper format */
    @Email
    private String email;

    /** User’s password, required for authentication */
    @NotBlank
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
