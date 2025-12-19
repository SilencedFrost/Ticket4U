package com.ticket4u.util;

import lombok.experimental.UtilityClass;

/**
 * Utility class for phone number operations
 */
@UtilityClass
public class PhoneNumberUtil {

    /**
     * Normalize phone number to standard format (remove spaces, convert +84 to 0)
     *
     * @param phoneNumber the phone number to normalize
     * @return normalized phone number
     */
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
