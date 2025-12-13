package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final JobRepository jobRepository;

    public AdminService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Returns all jobs waiting for admin review.
     */
    public List<Job> getPendingJobs() {
        return jobRepository.findByStatus(Job.Status.PENDING);
    }

    /**
     * Approves a job posting.
     */
    public void approveJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(Job.Status.APPROVED);
        jobRepository.save(job);
    }

    /**
     * Rejects a job posting.
     */
    public void rejectJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(Job.Status.REJECTED);
        jobRepository.save(job);
    }
}
