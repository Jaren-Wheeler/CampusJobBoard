package com.example.CampusJobBoard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO used when a SUPER_ADMIN creates a new ADMIN account.
 * Includes full field-level validation to ensure proper input structure.
 */
public class CreateAdminRequest {

    /** Full name of the new admin (first and last). */
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z]+( [A-Za-z]+)+$",
            message = "Enter a valid full name (first and last)"
    )
    private String fullName;

    /** Email address the new admin will use to log in. */
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    /** Raw password for the new admin. */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$",
            message = "Password must contain upper, lower, number, and special character"
    )
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
