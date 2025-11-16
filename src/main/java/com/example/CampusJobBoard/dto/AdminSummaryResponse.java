package com.example.CampusJobBoard.dto;

/**
 * Lightweight view of an Admin user that is safe to send to the frontend.
 * Exposes only the fields the Super Admin dashboard needs.
 */
public class AdminSummaryResponse {

    private Long id;
    private String fullName;
    private String email;

    public AdminSummaryResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() { return id; }

    public String getFullName() { return fullName; }

    public String getEmail() { return email; }
}
