package com.ticket4u.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException() {
        super("auth.error.email_already_used");
    }
}