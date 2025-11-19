package com.example.CampusJobBoard.dto;

/**
 * View of Admin used by the Super Admin dashboard.
 */
public record AdminSummaryResponse(
        Long id,
        String fullName,
        String email
) {}
