package com.ticket4u.exception;

public class JwtProcessingException extends Exception {
    public JwtProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtProcessingException(String message) {
        super(message);
    }
}
