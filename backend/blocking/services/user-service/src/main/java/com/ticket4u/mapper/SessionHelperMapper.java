package com.ticket4u.mapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionHelperMapper {

    private static final int DISPLAY_ID_LENGTH = 6;

    @Named("toDisplayId")
    public String toDisplayId(UUID sessionId) {
        if (sessionId == null) {
            return null;
        }
        return DigestUtils.sha256Hex(sessionId.toString()).substring(0, DISPLAY_ID_LENGTH);
    }

    @Named("normalizeUserAgent")
    public String normalizeUserAgent(String userAgent) {
        return (userAgent == null || userAgent.isBlank()) ? "Unknown" : userAgent;
    }
}
