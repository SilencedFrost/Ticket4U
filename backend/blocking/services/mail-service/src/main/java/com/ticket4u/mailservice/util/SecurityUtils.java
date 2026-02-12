package com.ticket4u.mailservice.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

    public static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : request.getRemoteAddr();
    }

    public static String maskApiKey(String key) {
        if (key == null || key.length() < 8) return "***";
        return key.substring(0, 8) + "...";
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        int atIdx = email.indexOf("@");
        return email.substring(0, Math.min(3, atIdx)) + "***@" + email.substring(atIdx + 1);
    }
}
