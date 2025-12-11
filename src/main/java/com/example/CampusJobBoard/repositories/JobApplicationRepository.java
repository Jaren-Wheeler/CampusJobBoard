package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    // Finds all job applications by id
    Optional<JobApplication> findById(Long jobApplicationId);

    // lists all job applications corresponding to specific user id
    List<JobApplication> findByUser(User user);

    // checks if job application already exists for a certain job and user id.
    boolean existsByUserAndJob(User user, Job job);
}
