package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.services.ApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Handles all page-level routing for the frontend.
 * This controller serves Thymeleaf template files for:
 * - Login page
 * - Dashboards for Student, Employer, Admin
 */
@Controller
public class ViewController {

    private final ApplicationService appService;

    public ViewController(ApplicationService appService) {
        this.appService = appService;
    }

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
    public String studentDash(Model model) {
        List<Job> jobs = appService.getApprovedJobs();
        model.addAttribute("jobs",jobs);

        model.addAttribute("jobApplication", new JobApplication());
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

    @GetMapping("/superadmin/setup")
    public String superAdminSetupPage() {
        return "superadmin/setup";
    }

}
