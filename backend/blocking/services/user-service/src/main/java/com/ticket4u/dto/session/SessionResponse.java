package com.ticket4u.dto.session;

import java.time.OffsetDateTime;

public record SessionResponse(
        String sessionId,
        String deviceClient, //user_agent
        OffsetDateTime lastAccess //update_at
) {}
