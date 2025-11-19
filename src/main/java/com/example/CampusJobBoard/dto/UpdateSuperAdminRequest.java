package com.example.CampusJobBoard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used when the SUPER_ADMIN updates their own account profile.
 * Allows updating:
 *  - fullName
 *  - email
 *  - password
 *
 * Validation ensures that incoming data is well-formed before reaching the service layer.
 */
public class UpdateSuperAdminRequest {

    /** Updated full name for the Super Admin */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** Updated email address */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /** New password (optional but must meet minimum length if provided) */
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

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
