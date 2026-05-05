package com.ticket4u.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNumberUtil {

    public static String normalize(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return phoneNumber;
        }

        String cleaned = phoneNumber.replaceAll("[\\s-]", "");
        
        if (cleaned.length() == 9 && cleaned.matches("^(3|5|7|8|9)\\d{8}$")) {
            return "0" + cleaned;
        }
        
        return cleaned;
    }
}
