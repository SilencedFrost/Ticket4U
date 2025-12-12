package com.ticket4u.exception;

public class ConcurrentRequestException extends RuntimeException {
    public ConcurrentRequestException(String message) {
        super(message);
    }
}
