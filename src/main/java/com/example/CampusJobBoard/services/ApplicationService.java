package com.example.CampusJobBoard.services;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.JobApplicationRepository;
import com.example.CampusJobBoard.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationService(JobApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    // returns a list of all the jobs approved by admin
    public List<Job> getApprovedJobs() {
        List<Job> jobs = jobRepository.findByStatus(Job.Status.APPROVED);
        return jobs;
    }

    // submits the job application
    public JobApplication submit(JobApplication application) {
        return applicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }
}
