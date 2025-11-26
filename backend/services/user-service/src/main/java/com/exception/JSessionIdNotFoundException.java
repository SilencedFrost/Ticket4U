package com.exception;

public class JSessionIdNotFoundException extends RuntimeException {
    public JSessionIdNotFoundException(String message) {
        super(message);
    }
}
