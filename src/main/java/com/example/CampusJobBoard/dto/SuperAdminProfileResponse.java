package com.example.CampusJobBoard.dto;

/**
 * DTO representing basic profile information for the Super Admin.
 */
public record SuperAdminProfileResponse(
        String fullName,
        String email
) {}
