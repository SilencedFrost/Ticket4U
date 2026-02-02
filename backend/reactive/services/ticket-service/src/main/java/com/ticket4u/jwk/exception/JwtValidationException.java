package com.ticket4u.jwk.exception;

public class JwtValidationException extends Exception {
    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtValidationException(String message) {
        super(message);
    }
}
