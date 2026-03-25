package com.ticket4u.management.dto;

import com.ticket4u.core.entity.Event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ManagementEventResponse(
        UUID id,
        Event.EventStatus status,
        // Layout — not in public EventResponse
        String layout,
        // Stats — management only
        Integer ticketsSold,
        Integer totalCapacity,
        BigDecimal revenue,
        // Sessions — management needs full session list with IDs
        List<SessionSummary> sessions
) {
    public record SessionSummary(UUID id, OffsetDateTime startDate, OffsetDateTime endDate) {}
}