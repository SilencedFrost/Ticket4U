package com.dto.session;

import java.time.OffsetDateTime;

public record SessionUpdateRequest(
        Long sessionId,
        OffsetDateTime lastAccessed,
        OffsetDateTime expiresAt,
        Boolean isActive,
        OffsetDateTime revokedAt,
        String revokeReason,
        String userAgent
) {
}
