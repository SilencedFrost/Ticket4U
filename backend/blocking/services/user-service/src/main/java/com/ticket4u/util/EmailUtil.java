package com.ticket4u.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailUtil {

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
