package com.example.CampusJobBoard.repositories;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByUser_UserId(Long userId);

    // find job by its status
    List<Job> findByStatus(Job.Status status);


}
