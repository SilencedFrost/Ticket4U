package com.dto.session;

import java.time.OffsetDateTime;

public record SessionResponse(
        Long sessionId,
        Long userId,
        OffsetDateTime lastAccessed,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt,
        Boolean isActive,
        OffsetDateTime revokedAt,
        String revokeReason,
        String userAgent
) {
}
