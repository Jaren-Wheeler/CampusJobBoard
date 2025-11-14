package com.example.CampusJobBoard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all page-level routing for the frontend.
 * This controller serves Thymeleaf template files for:
 * - Login page
 * - Student dashboard
 * - Employer dashboard
 * - Admin dashboard
 * No backend data is injected here.
 * These pages act as shells which load JavaScript that
 * fetches real data from the API layer (/api/...).
 */
@Controller
public class ViewController {

    /**
     * Displays the login/register page.
     * Template path: src/main/resources/templates/auth/login.html
     * URL: GET /login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Displays the student dashboard page.
     * Template path: templates/student/dashboard.html
     * URL: GET /student/dashboard
     */
    @GetMapping("/student/dashboard")
    public String studentDash() {
        return "student/dashboard";
    }

    /**
     * Displays the employer dashboard page.
     * Template path: templates/employer/dashboard.html
     * URL: GET /employer/dashboard
     */
    @GetMapping("/employer/dashboard")
    public String employerDash() {
        return "employer/dashboard";
    }

    /**
     * Displays the admin dashboard page.
     * Template path: templates/admin/dashboard.html
     * URL: GET /admin/dashboard
     */
    @GetMapping("/admin/dashboard")
    public String adminDash() {
        return "admin/dashboard";
    }
}
