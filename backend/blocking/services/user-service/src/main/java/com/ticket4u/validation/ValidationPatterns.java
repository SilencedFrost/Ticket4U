package com.ticket4u.validation;

public interface ValidationPatterns {
    String PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$";

    String PHONE_NUMBER = "^(0\\d{9}|[1-9]\\d{8})$";

    // Does not validate null values unlike @NotBlank
    String NON_BLANK = ".*\\S.*";
}
