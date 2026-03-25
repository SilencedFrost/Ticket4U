package com.ticket4u.util;

import static com.ticket4u.constant.MailNormalization.*;
import com.ticket4u.exception.NotAnEmailException;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class EmailUtil {

    private static boolean validateEmailFormat(String email) {
        if(email == null || email.isBlank()) return false;

        int firstAt = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');

        // Check if @ is missing, is the first/last char, or if there are multiple
        return firstAt > 0 && firstAt == lastAt && firstAt != email.length() - 1;
    }

    private static boolean handleSafety(String email, boolean safe) {
        boolean isValid = validateEmailFormat(email);

        if (!isValid) {
            if (safe) return false;
            throw new NotAnEmailException(String.format("%s is not an email!", email));
        }

        return true;
    }

    public static String getLocalPart(String email, boolean safe) {
        if(!handleSafety(email, safe)) return null;
        return email.substring(0, email.indexOf('@')).trim();
    }

    public static String getDomain(String email, boolean safe) {
        if(!handleSafety(email, safe)) return null;
        return email.substring(email.indexOf('@') + 1).trim();
    }

    public static String normalizeEmail(String email) {
        String localPart = getLocalPart(email.trim(), true);
        String domain = getDomain(email.trim(), true);

        if(localPart == null || domain == null) return null;

        List<NORMALIZATION_RULE> activeRules = NORMALIZATION_MAPPING.get(domain.toLowerCase());

        if(activeRules == null || activeRules.isEmpty()) return null;

        for(NORMALIZATION_RULE currentRule: NORMALIZATION_ORDER) {

            if(activeRules.contains(currentRule)) {
                // Special case for alias resolve, after resolving alias, refresh active ruleset to use canonical's ruleset
                if(currentRule.equals(NORMALIZATION_RULE.RESOLVE_ALIAS_DOMAIN)) {
                    domain = ALIAS_DOMAIN_LOOKUP.get(domain.toLowerCase());
                    activeRules = NORMALIZATION_MAPPING.get(domain.toLowerCase());
                // Switch case processing for every case
                } else {
                    switch (currentRule) {
                        case LOWERCASE_DOMAIN -> domain = domain.toLowerCase();
                        case LOWERCASE_LOCAL_PART -> localPart = localPart.toLowerCase();
                        case REPLACE_HYPHEN_WITH_DOT -> localPart = localPart.replace("-", ".");
                        case STRIP_SUFFIX -> {
                            // Find the earliest occurrence of any character in SUFFIX_LIST
                            int index = SUFFIX_LIST.stream()
                                    .mapToInt(localPart::indexOf)
                                    .filter(i -> i >= 0) // Ignore suffixes not found
                                    .min()               // Get the one that appears first
                                    .orElse(-1);
                            if (index != -1) {
                                localPart = localPart.substring(0, index);
                            }
                        }
                        case REMOVE_DOTS_IN_LOCAL_PART -> localPart = localPart.replace(".", "");
                    }
                }
                if(localPart.isBlank()) return null;
            }
        }

        return localPart + "@" + domain;
    }
}
