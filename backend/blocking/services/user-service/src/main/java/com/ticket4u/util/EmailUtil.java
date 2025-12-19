package com.ticket4u.util;

import lombok.experimental.UtilityClass;

/**
 * Utility class for email operations
 */
@UtilityClass
public class EmailUtil {

    /**
     * Extract username from email address (part before @)
     *
     * @param email the email address
     * @return username extracted from email
     */
    public static String extractUsername(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            return email.substring(0, atIndex);
        }

        return email;
    }
}
