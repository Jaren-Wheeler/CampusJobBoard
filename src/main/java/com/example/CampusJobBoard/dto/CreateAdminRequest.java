package com.example.CampusJobBoard.dto;

/**
 * Data Transfer Object used when a SUPER_ADMIN requests
 * to create a new ADMIN user account.
 */
public class CreateAdminRequest {

    /** The full name of the new admin being created. */
    private String fullName;

    /** The email address the new admin will use to log in. */
    private String email;

    /** The raw password provided during admin creation (hashed later). */
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
