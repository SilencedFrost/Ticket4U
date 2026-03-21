package com.ticket4u.jwk.exception;

public class JwkRetrievalException extends Exception {

    public JwkRetrievalException(String message) {
        super(message);
    }

    public JwkRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
