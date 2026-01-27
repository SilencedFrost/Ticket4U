package com.ticket4u.exception;

public class LogoutFailedException extends RuntimeException{
    private final String i18nKey;

    public LogoutFailedException(String message, String i18nKey) {
        super(message);
        this.i18nKey = i18nKey;
    }

    public LogoutFailedException(String message, String i18nKey, Throwable cause) {
        super(message, cause);
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
