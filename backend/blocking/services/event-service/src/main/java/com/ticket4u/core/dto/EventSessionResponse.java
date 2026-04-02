package com.ticket4u.core.dto;

import com.ticket4u.core.entity.EventSession;

import java.time.OffsetDateTime;

public record EventSessionResponse(
        String id,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        EventSession.SessionStatus status,
        String name
) {
}
