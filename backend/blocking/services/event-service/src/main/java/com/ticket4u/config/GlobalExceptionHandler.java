package com.ticket4u.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String message;

        if (ex.getRequiredType() != null && ex.getRequiredType().equals(LocalDate.class)) {
            message = String.format("Invalid date format for '%s'. Expected format: yyyy-MM-dd (e.g. 2026-03-04)", paramName);
        } else {
            message = String.format("Invalid value for parameter '%s': %s", paramName, ex.getValue());
        }

        return ResponseEntity.badRequest().body(Map.of(
                "error", "Bad Request",
                "message", message
        ));
    }
}
