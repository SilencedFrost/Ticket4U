package com.ticket4u.feature.homepage.dto;

import java.util.List;

/**
 * Response DTO for category with its events
 * Used for homepage category sections
 */
public record CategoryWithEventsResponse(
        Integer id,
        String name,
        List<EventSummaryResponse> events
) {}
