package com.example.CampusJobBoard.exceptions;

import java.util.Map;

/**
 * Custom exception used to report field-level validation errors.
 * Stores a map of field → error message pairs that can be returned
 * directly to the client or handled by a global exception handler.
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
