package com.ticket4u.exception;

public class PhoneNumberAlreadyExistException extends RuntimeException {
    public PhoneNumberAlreadyExistException(String message) {
        super(message);
    }
}
