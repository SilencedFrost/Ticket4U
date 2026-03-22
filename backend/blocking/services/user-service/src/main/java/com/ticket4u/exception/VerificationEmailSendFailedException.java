package com.ticket4u.exception;

public class VerificationEmailSendFailedException extends RuntimeException {
    public VerificationEmailSendFailedException(String message) {
        super(message);
    }
}
