package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.services.ApplicationService;
import com.example.CampusJobBoard.services.JobService;
import com.example.CampusJobBoard.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final ApplicationService appService;
    private final UserService userService;
    private final JobService jobService;

    public StudentController(ApplicationService appService,
                             UserService userService,
                             JobService jobService) {
        this.appService = appService;
        this.userService = userService;
        this.jobService = jobService;
    }

    // Return all approved jobs
    @GetMapping
    public List<Job> getAllJobs() {
        return appService.getApprovedJobs();
    }

    // Submit application
    @PostMapping("/submit")
    public ResponseEntity<String> submitApplication(
            @RequestParam Long JobId,
            Principal principal) {

        User user = userService.findByEmail(principal.getName());
        Job job = jobService.findJobById(JobId);

        JobApplication app = new JobApplication();
        app.setUser(user);
        app.setJob(job);

        appService.submit(app);

        return ResponseEntity.ok("Application submitted successfully!");
    }

    // Get current user's applications
    @GetMapping("/applications")
    public List<JobApplication> getMyApps(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return appService.getApplicationsByUser(user);
    }
}

