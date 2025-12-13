package com.example.CampusJobBoard.exceptions;

public class DuplicateApplicationException extends RuntimeException {

    public DuplicateApplicationException() {
        super("You have already applied to this job.");
    }
}
