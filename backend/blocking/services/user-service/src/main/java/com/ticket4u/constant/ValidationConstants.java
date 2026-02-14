package com.ticket4u.constant;

import java.util.Set;

public class ValidationConstants {
    public static final Set<String> ALLOWED_EMAIL_DOMAIN = Set.of(
            // Most popular in Vietnam
            "gmail.com",
            "outlook.com",
            "hotmail.com",
            "live.com",
            "yahoo.com",
            "yahoo.com.vn",

            // Apple devices are popular in Vietnam
            "icloud.com",
            "me.com",
            "mac.com",

            // Other Microsoft domains
            "msn.com",
            "outlook.co.uk",
            "hotmail.co.uk",
            "live.co.uk",

            // Privacy-focused
            "protonmail.com",
            "proton.me",

            // Other legitimate providers occasionally used
            "aol.com",
            "zoho.com",
            "yandex.com"
    );
}
