package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.services.ApplicationService;
import com.example.CampusJobBoard.services.JobService;
import com.example.CampusJobBoard.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
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
    private final UserService userService;
    private final JobService jobService;

    public ViewController(ApplicationService appService, UserService userService, JobService jobService) {
        this.appService = appService;
        this.userService = userService;
        this.jobService = jobService;
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
    public String dashboard(Model model, Authentication authentication) {

        /*if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }*/
        System.out.println("AUTH = " + authentication);
        System.out.println("AUTHORITIES = " + authentication.getAuthorities());

        String email = authentication.getName();  // always works if JWT is configured correctly
        User user = userService.findByEmail(email);

        model.addAttribute("loggedInUser", user);
        model.addAttribute("jobs", jobService.getApprovedJobs());

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
