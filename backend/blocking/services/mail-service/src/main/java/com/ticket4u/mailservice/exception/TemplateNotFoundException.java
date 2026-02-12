package com.ticket4u.mailservice.exception;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
