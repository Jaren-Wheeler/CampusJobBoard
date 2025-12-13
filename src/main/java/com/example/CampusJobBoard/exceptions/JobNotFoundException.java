package com.example.CampusJobBoard.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(Long id) {
        super("Job not found with id: " + id);
    }
}
