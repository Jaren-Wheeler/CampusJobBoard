package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.dto.RegisterRequest;
import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.services.ApplicationService;
import com.example.CampusJobBoard.services.AuthService;
import com.example.CampusJobBoard.services.JobService;
import com.example.CampusJobBoard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final AuthService authService;
    public ViewController(ApplicationService appService, UserService userService, JobService jobService,
                            AuthService authService)
    {
        this.appService = appService;
        this.userService = userService;
        this.jobService = jobService;
        this.authService = authService;
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

    @PostMapping("/register")
    public String register(RegisterRequest request) {
        authService.register(request);
        return "redirect:/login?registered";
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

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("jobs", jobService.getApprovedJobs());

        return "student/dashboard";
    }


    @GetMapping("/student/myApplications")
    public String studentMyApplications(Model model, Authentication authentication) {

        // Get logged-in user's email
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // Get their applications
        List<JobApplication> apps = appService.getApplicationsByUser(user);

        // Give to Thymeleaf
        model.addAttribute("applications", apps);
        model.addAttribute("loggedInUser", user);

        return "student/myApplications";
    }


    /**
     * Employer dashboard.
     */
    @GetMapping("/employer/dashboard")
    public String employerDash(Model model, Authentication authentication) {
        System.out.println("AUTH = " + authentication);
        System.out.println("AUTHORITIES = " + authentication.getAuthorities());

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("jobs", jobService.getApprovedJobs());
        return "employer/dashboard";
    }

    @PostMapping("/student/submit")
    public String submitApplication(@RequestParam Long JobId,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(principal.getName());
        Job job = jobService.findJobById(JobId);

        JobApplication app = new JobApplication();
        app.setUser(user);
        app.setJob(job);

        try {
            appService.submit(app);
            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student/dashboard";
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
    public String superAdminDash(Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if (user.getEmail().equals("superadmin@system.com")) {
            return "redirect:/superadmin/setup";
        }

        return "superadmin/dashboard";
    }


    @GetMapping("/superadmin/setup")
    public String superAdminSetupPage() {
        return "superadmin/setup";
    }

}
