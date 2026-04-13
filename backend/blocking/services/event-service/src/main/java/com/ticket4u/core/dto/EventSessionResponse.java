package com.ticket4u.core.dto;

import com.ticket4u.core.entity.EventSession;

import java.time.OffsetDateTime;
import java.util.Set;

public record EventSessionResponse(
        String id,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        EventSession.SessionStatus status,
        String name,
        Set<ZoneResponse> zones
) {
}
