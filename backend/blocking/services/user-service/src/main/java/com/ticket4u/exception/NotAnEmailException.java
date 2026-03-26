package com.ticket4u.exception;

public class NotAnEmailException extends RuntimeException {
    public NotAnEmailException(String message) {
        super(message);
    }
}
