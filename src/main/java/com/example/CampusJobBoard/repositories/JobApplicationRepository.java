package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.JobApplication;
import com.example.CampusJobBoard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findById(Long jobApplicationId);

    List<JobApplication> findByUser(User user);
}
