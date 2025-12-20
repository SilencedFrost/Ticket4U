package com.ticket4u.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNumberUtil {

    public static String normalize(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return phoneNumber;
        }

        String cleaned = phoneNumber.replaceAll("[\\s-]", "");
        if (cleaned.startsWith("+84")) {
            return "0" + cleaned.substring(3);
        } else if (cleaned.startsWith("84")) {
            return "0" + cleaned.substring(2);
        }
        return cleaned;
    }
}
