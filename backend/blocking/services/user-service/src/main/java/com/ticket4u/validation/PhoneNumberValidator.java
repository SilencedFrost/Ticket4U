package com.ticket4u.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // Vietnam phone patterns
    private static final Pattern VN_MOBILE_PATTERN = Pattern.compile("^(\\+84|84|0)(3|5|7|8|9)\\d{8}$");
    
    // VoIP prefixes to block (VN)
    private static final Set<String> VOIP_PREFIXES = Set.of("2", "6");

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        // Null or empty is valid (use @NotBlank for required check)
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        // Remove spaces and dashes
        String cleaned = phoneNumber.replaceAll("[\\s-]", "");

        // Check Vietnam format
        if (!VN_MOBILE_PATTERN.matcher(cleaned).matches()) {
            log.debug("Phone number {} does not match Vietnam mobile pattern", phoneNumber);
            return false;
        }

        // Extract prefix after country code
        String prefix = extractPrefix(cleaned);
        
        // Block VoIP numbers
        if (VOIP_PREFIXES.contains(prefix)) {
            log.debug("Phone number {} is VoIP (prefix: {})", phoneNumber, prefix);
            return false;
        }

        return true;
    }

    private String extractPrefix(String cleaned) {
        // Handle +84, 84, or 0 prefix
        if (cleaned.startsWith("+84")) {
            return cleaned.substring(3, 4);
        } else if (cleaned.startsWith("84")) {
            return cleaned.substring(2, 3);
        } else if (cleaned.startsWith("0")) {
            return cleaned.substring(1, 2);
        }
        return "";
    }
}
