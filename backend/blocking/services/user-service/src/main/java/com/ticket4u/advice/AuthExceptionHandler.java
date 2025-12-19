package com.ticket4u.advice;

import com.ticket4u.exception.EmailAlreadyExistException;
import com.ticket4u.exception.InvalidGoogleTokenException;
import com.ticket4u.exception.PhoneNumberAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401
                .body(Map.of("message", "Invalid credentials"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabled(DisabledException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)  // 403
                .body(Map.of("message", "Account disabled"));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<?> handleEmailAlreadyExist(EmailAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)  // 409
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(PhoneNumberAlreadyExistException.class)
    public ResponseEntity<?> handlePhoneNumberAlreadyExist(PhoneNumberAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)  // 409
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<?> handleInvalidGoogleToken(InvalidGoogleTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401
                .body(Map.of("message", e.getMessage()));
    }
}
