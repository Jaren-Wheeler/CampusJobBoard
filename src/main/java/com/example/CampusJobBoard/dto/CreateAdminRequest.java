package com.example.CampusJobBoard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used when a SUPER_ADMIN creates a new ADMIN account.
 * Includes field-level validation to ensure predictable and safe inputs.
 */
public class CreateAdminRequest {

    /** The full name of the new admin being created. */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** The email address the new admin will use to log in. */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /** The raw password provided during admin creation. */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
