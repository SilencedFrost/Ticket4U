package com.ticket4u.mailservice.exception;

import com.ticket4u.mailservice.dto.response.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<EmailResponse> handleTemplateNotFound(TemplateNotFoundException ex) {
        log.warn("Template not found: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(EmailResponse.failed(ex.getMessage()));
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<EmailResponse> handleEmailSendException(EmailSendException ex) {
        log.error("Email send failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(EmailResponse.failed(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmailResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(EmailResponse.failed(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmailResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(EmailResponse.failed("Internal server error"));
    }
}
