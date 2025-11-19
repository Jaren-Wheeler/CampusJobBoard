package com.example.CampusJobBoard.dto;

import com.example.CampusJobBoard.entities.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest is a Data Transfer Object used when a new user signs up for an account.
 * Includes:
 * - Full name
 * - Email
 * - Password
 * - Role (STUDENT, EMPLOYER, or ADMIN)
 * Validation annotations ensure all essential fields are provided and formatted correctly
 * before creating a new user record.
 */
public class RegisterRequest {

    /** User’s full name, required during registration */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** User’s email address, must follow valid format */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /** Account password, cannot be blank and must meet security minimums */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /** Role assigned at registration (STUDENT or EMPLOYER) */
    private Role role;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
