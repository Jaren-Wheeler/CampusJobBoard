package com.example.CampusJobBoard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all page-level routing for the frontend.
 * This controller serves Thymeleaf template files for:
 * - Login page
 * - Dashboards for Student, Employer, Admin
 */
@Controller
public class ViewController {

    /**
     * Default root route.
     * Visiting "/" redirects user to /login.
     */
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    /**
     * Displays the login/register page.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Student dashboard.
     */
    @GetMapping("/student/dashboard")
    public String studentDash() {
        return "student/dashboard";
    }

    @GetMapping("/student/myApplications")
    public String studentMyApplications() {
        return "student/myApplications";
    }

    /**
     * Employer dashboard.
     */
    @GetMapping("/employer/dashboard")
    public String employerDash() {
        return "employer/dashboard";
    }

    /**
     * Admin dashboard.
     */
    @GetMapping("/admin/dashboard")
    public String adminDash() {
        return "admin/dashboard";
    }

    /**
     * Super_Admin dashboard.
     */
    @GetMapping("/superadmin/dashboard")
    public String superAdminDash() {
        return "superadmin/dashboard";
    }

}
