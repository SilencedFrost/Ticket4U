package com.ticket4u.advice;

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
        Map<String, Map<String, String>> nestedErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> {
            String field = err.getField();
            String message = err.getDefaultMessage();

            String[] parts = field.split("\\.", 2);
            if (parts.length > 1) {
                nestedErrors
                        .computeIfAbsent(parts[0], key -> new HashMap<>())
                        .put(parts[1], message);
            } else {
                errors.put(field, message);
            }
        });

        errors.putAll(nestedErrors);

        return ResponseEntity.badRequest().body(errors);
    }
}
