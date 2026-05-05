package com.ticket4u.validation;

public interface ValidationPatterns {
    // from 8 to 32 chars long, have at least one of each category: lowercase, uppercase, number, characters from the set of !@$^*()-_=+[]{}\|;:",./?~`
    String PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@$^*()_=+\\[\\]{}\\\\|;:\",.\\/?~`-]).{8,32}$";

    // May start with a 0, followed by 3, 5, 7, 8, 9, ends with another 8 numbers
    String PHONE_NUMBER = "^0?(3|5|7|8|9)\\d{8}$";

    // Does not validate null values unlike @NotBlank
    String NON_BLANK = ".*\\S.*";
}
