package com.ticket4u.constant;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MailNormalization {
    public static final Set<String> ALLOWED_EMAIL_DOMAIN = Set.of(
            // Most popular in Vietnam
            "gmail.com",
            "googlemail.com",
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

    public static enum NORMALIZATION_RULE {

        /**
         * Treat uppercase and lowercase letters as identical.
         * e.g. User@Example.com → user@example.com
         */
        LOWERCASE_LOCAL_PART,

        /**
         * Treat uppercase and lowercase letters in the domain as identical.
         * e.g. user@Gmail.COM → user@gmail.com
         */
        LOWERCASE_DOMAIN,

        /**
         * Remove all dots from the local part before comparison.
         * e.g. u.s.e.r@gmail.com → user@gmail.com
         */
        REMOVE_DOTS_IN_LOCAL_PART,

        /**
         * Remove everything from the first '+' or '-' onward in the local part.
         * e.g. user+newsletter@gmail.com → user@gmail.com
         */
        STRIP_SUFFIX,

        /**
         * Treat multiple domains as the same canonical identity (alias domains).
         * e.g. @icloud.com, @me.com, @mac.com → @icloud.com
         * e.g. @protonmail.com, @proton.me → @protonmail.com
         */
        RESOLVE_ALIAS_DOMAIN,

        /**
         * Yahoo disposable mails use hyphens
         * Yandex treat hyphen same as plus suffix
         */
        REPLACE_HYPHEN_WITH_DOT
    }

    public static List<NORMALIZATION_RULE> NORMALIZATION_ORDER = List.of(
            NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN,
            NORMALIZATION_RULE.LOWERCASE_DOMAIN,
            NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
            NORMALIZATION_RULE.REPLACE_HYPHEN_WITH_DOT,
            NORMALIZATION_RULE.STRIP_SUFFIX,
            NORMALIZATION_RULE.REMOVE_DOTS_IN_LOCAL_PART
    );

    public static List<String> SUFFIX_LIST = List.of("=", "+", "-");

    /**
     * Maps every alias domain (and the canonical domain itself) to its canonical form.
     * Allows O(1) normalization: ALIAS_DOMAIN_LOOKUP.getOrDefault(domain, domain)
     */
    public static final Map<String, String> ALIAS_DOMAIN_LOOKUP = Map.of(
            // Apple — icloud.com is the canonical domain
            "me.com",  "icloud.com",
            "mac.com", "icloud.com",

            // Proton — protonmail.com is the canonical domain
            "proton.me", "protonmail.com",

            // Microsoft — all route to the same mailbox system, outlook.com is canonical
            "hotmail.com",    "outlook.com",
            "live.com",       "outlook.com",
            "msn.com",        "outlook.com",
            "hotmail.co.uk",  "outlook.com",
            "live.co.uk",     "outlook.com",
            "outlook.co.uk",  "outlook.com"
    );

    public static final Map<String, List<NORMALIZATION_RULE>> NORMALIZATION_MAPPING = Map.ofEntries(

        // Gmail & aliases
        Map.entry("gmail.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.REMOVE_DOTS_IN_LOCAL_PART,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),
        Map.entry("googlemail.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),

        // Microsoft outlook & aliases
        Map.entry("outlook.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),
        Map.entry("hotmail.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("live.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("msn.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("outlook.co.uk", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("hotmail.co.uk", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("live.co.uk", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),

        // Yahoo and yahoo.vn are different
        Map.entry("yahoo.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),
        Map.entry("yahoo.com.vn", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),

        // Apple icloud & aliases
        Map.entry("icloud.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),
        Map.entry("me.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),
        Map.entry("mac.com", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),

        // Protonmail & aliases
        Map.entry("protonmail.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.REMOVE_DOTS_IN_LOCAL_PART,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),
        Map.entry("proton.me", List.of(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)),

        //
        Map.entry("aol.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN
        )),

        // Zoho
        Map.entry("zoho.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.STRIP_SUFFIX
        )),

        // Yandex
        Map.entry("yandex.com", List.of(
                NORMALIZATION_RULE.LOWERCASE_LOCAL_PART,
                NORMALIZATION_RULE.LOWERCASE_DOMAIN,
                NORMALIZATION_RULE.REPLACE_HYPHEN_WITH_DOT,
                NORMALIZATION_RULE.STRIP_SUFFIX
        ))
    );
}
