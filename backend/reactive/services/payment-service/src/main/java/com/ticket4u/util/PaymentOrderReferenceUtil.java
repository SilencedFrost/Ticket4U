package com.ticket4u.util;

import com.ticket4u.config.PaymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PaymentOrderReferenceUtil {

    private static final Pattern UUID_DASHED_PATTERN = Pattern
            .compile("(?i)\\b([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\\b");
    private static final Pattern UUID_COMPACT_PATTERN = Pattern
            .compile("(?i)\\b([0-9a-f]{32})\\b");

    private final PaymentProperties paymentProperties;

    public Optional<UUID> extractOrderId(String content) {
        if (!StringUtils.hasText(content)) {
            return Optional.empty();
        }

        String prefix = paymentProperties.resolveOrderCodePrefix();
        Pattern prefixedPattern = Pattern.compile(
                "(?i)\\b" + Pattern.quote(prefix)
                        + "(?:\\s+|-)?([0-9a-f]{8}(?:-?[0-9a-f]{4}){3}-?[0-9a-f]{12})\\b");

        Matcher prefixedMatcher = prefixedPattern.matcher(content);
        if (prefixedMatcher.find()) {
            return parseUuid(prefixedMatcher.group(1));
        }

        Matcher dashedMatcher = UUID_DASHED_PATTERN.matcher(content);
        if (dashedMatcher.find()) {
            return parseUuid(dashedMatcher.group(1));
        }

        Matcher compactMatcher = UUID_COMPACT_PATTERN.matcher(content);
        if (compactMatcher.find()) {
            return parseUuid(compactMatcher.group(1));
        }

        return Optional.empty();
    }

    private Optional<UUID> parseUuid(String value) {
        try {
            String normalized = value == null ? "" : value.trim();

            if (normalized.matches("(?i)^[0-9a-f]{32}$")) {
                normalized = normalized.substring(0, 8)
                        + "-" + normalized.substring(8, 12)
                        + "-" + normalized.substring(12, 16)
                        + "-" + normalized.substring(16, 20)
                        + "-" + normalized.substring(20);
            }

            return Optional.of(UUID.fromString(normalized));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
