package com.ticket4u.validation;

public interface ValidationPatterns {
    /**
     * - At least one uppercase letter
     * - At least one special character
     * - Length between 8-32 characters
     */
    String PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$";

    //  Vietnamese phone number format: Starting with 0 followed by 9 digits
    String PHONE_NUMBER = "^(0\\d{9}|[1-9]\\d{8})$";
    String NON_BLANK = ".*\\S.*";
}
