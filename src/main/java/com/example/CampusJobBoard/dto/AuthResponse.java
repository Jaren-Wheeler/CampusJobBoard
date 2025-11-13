package com.example.CampusJobBoard.dto;

/**
 * AuthResponse is a Data Transfer Object
 * used to send authentication results back to the client.
 * Contains:
 * - A token
 * - The user’s assigned role (STUDENT, EMPLOYER, or ADMIN)
 */
public class AuthResponse {

    /** Token returned after successful registration or login */
    private String token;

    /** The user's role within the system */
    private String role;

    /** Default constructor */
    public AuthResponse() {}

    /** Convenience constructor for creating quick responses */
    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
