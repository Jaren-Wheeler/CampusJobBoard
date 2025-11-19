package com.example.CampusJobBoard.dto;

/**
 * DTO returned after a successful registration or login operation. It contains:
 * - A JWT token for authenticated requests
 * - The user's role
 * - A flag indicating whether the user must update their profile
 *   (used for forcing the Super Admin Setup page)
 */
public class AuthResponse {

    private String token;

    private String role;

    private boolean mustUpdateProfile;

    public AuthResponse() {}

    /**
     * Constructor used for standard registration and login
     * where no setup flag is needed.
     *
     * @param token The JWT authentication token
     * @param role  The user's assigned role
     */
    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
        this.mustUpdateProfile = false;
    }

    /**
     * Full constructor used when the backend needs to indicate
     * the user's setup status
     *
     * @param token              The JWT authentication token
     * @param role               The user's assigned role
     * @param mustUpdateProfile  Whether the user must update their profile
     */
    public AuthResponse(String token, String role, boolean mustUpdateProfile) {
        this.token = token;
        this.role = role;
        this.mustUpdateProfile = mustUpdateProfile;
    }

    // Getters and Setters

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isMustUpdateProfile() { return mustUpdateProfile; }
    public void setMustUpdateProfile(boolean mustUpdateProfile) {
        this.mustUpdateProfile = mustUpdateProfile;
    }
}
