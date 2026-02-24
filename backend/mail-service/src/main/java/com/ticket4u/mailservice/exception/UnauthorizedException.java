package com.ticket4u.mailservice.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String errorType;

    public UnauthorizedException(String message, String errorType) {
        super(message);
        this.errorType = errorType;
    }

    public static UnauthorizedException missingApiKey() {
        return new UnauthorizedException("Missing API Key", "MISSING_API_KEY");
    }

    public static UnauthorizedException invalidApiKey() {
        return new UnauthorizedException("Invalid API Key", "INVALID_API_KEY");
    }
}
