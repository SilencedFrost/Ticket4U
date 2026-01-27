package com.ticket4u.exception;

import com.ticket4u.dto.auth.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(LogoutFailedException.class)
    public ResponseEntity<?> handleLogoutFailed(LogoutFailedException e) {
        log.error("Logout failed - session not invalidated", e);

        // Return error message and i18n key
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),      // "Failed to invalidate session"
                e.getI18nKey()       // "error.logout.invalidation_failed"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}