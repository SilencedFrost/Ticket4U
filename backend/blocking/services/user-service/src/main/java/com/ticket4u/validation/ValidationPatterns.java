package com.ticket4u.validation;

public interface ValidationPatterns {
    String PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$";

    // May start with a 0, followed by 3, 5, 7, 8, 9, ends with another 8 numbers
    String PHONE_NUMBER = "^0?(3|5|7|8|9)\\d{8}$";

    // Does not validate null values unlike @NotBlank
    String NON_BLANK = ".*\\S.*";
}
