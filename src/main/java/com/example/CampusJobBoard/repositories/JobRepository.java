package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findById(Long JobId);

    // find job by its status
    List<Job> findByStatus(Job.Status status);
}
