package com.example.CampusJobBoard.dto;

import com.example.CampusJobBoard.entities.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * RegisterRequest is a Data Transfer Object used when a new user signs up for an account.
 * Includes:
 * - Full name
 * - Email
 * - Password
 * - Role (STUDENT, EMPLOYER, or ADMIN)
 * Validation annotations ensure all essential fields are provided and formatted correctly before creating a new user record.
 */
public class RegisterRequest {

    /** User’s full name, required during registration */
    @NotBlank
    private String fullName;

    /** User’s email address, must follow valid format */
    @Email
    private String email;

    /** Account password, cannot be blank */
    @NotBlank
    private String password;

    /** Role assigned at registration (Student/Employer) */
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
