package com.example.CampusJobBoard.exceptions;

import com.example.CampusJobBoard.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles validation and custom exceptions globally.
 * Ensures all validation errors are returned in a clean JSON format.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // DTO validation (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // Business rule validation
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleCustomValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getErrors());
    }
}

