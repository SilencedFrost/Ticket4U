package com.ticket4u.exception;

public class AccountAlreadyActiveException extends RuntimeException {
    public AccountAlreadyActiveException(String message) {
        super(message);
    }
}
