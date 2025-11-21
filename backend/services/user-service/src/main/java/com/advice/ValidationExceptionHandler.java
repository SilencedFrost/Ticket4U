package com.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> {
            String field = err.getField();
            String message = err.getDefaultMessage();

            String[] parts = field.split("\\.");
            if (parts.length > 1) {
                @SuppressWarnings("unchecked")
                Map<String, String> nestedErrors = (Map<String, String>) errors.computeIfAbsent(parts[0], k -> new HashMap<String, String>());
                nestedErrors.put(parts[1], message);
            } else {
                errors.put(field, message);
            }
        });

        return ResponseEntity.badRequest().body(errors);
    }
}