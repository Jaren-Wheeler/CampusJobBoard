package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Returns all jobs pending admin approval.
     */
    @GetMapping("/jobs/pending")
    public List<Job> getPendingJobs() {
        return adminService.getPendingJobs();
    }

    /**
     * Approves a job posting.
     */
    @PutMapping("/jobs/{id}/approve")
    public ResponseEntity<?> approveJob(@PathVariable Long id) {
        adminService.approveJob(id);
        return ResponseEntity.ok("Job approved");
    }

    /**
     * Rejects a job posting.
     */
    @PutMapping("/jobs/{id}/reject")
    public ResponseEntity<?> rejectJob(@PathVariable Long id) {
        adminService.rejectJob(id);
        return ResponseEntity.ok("Job rejected");
    }
}
