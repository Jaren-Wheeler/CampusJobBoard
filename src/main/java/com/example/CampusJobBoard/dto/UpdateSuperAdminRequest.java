package com.example.CampusJobBoard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO used when the SUPER_ADMIN completes their required
 * first-time account setup or updates their profile information.
 * All fields are mandatory during the initial setup:
 *  - fullName    → The system owner's real display name
 *  - email       → Must replace the temporary default email
 *  - password    → Must replace the default system password
 */
public class UpdateSuperAdminRequest {

    /** The updated full name of the Super Admin. */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** New email address for the Super Admin. */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * New password chosen by the Super Admin.
     */
    @NotBlank(message = "Password is required")
    private String password;

    // Getters + setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
