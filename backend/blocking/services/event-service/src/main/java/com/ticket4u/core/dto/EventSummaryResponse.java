package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Shared response DTO for a summarized event card (id, name, banner, dates, min price, category).
 * Core DTO used by homepage and event detail features
 * Use "Event.withAllEntities" entity graph
 */
public record EventSummaryResponse(
        UUID id,
        String name,
        UUID organizerId,
        // Category fields flat mapped from category
        Integer categoryId,
        String categoryName,
        Event.EventStatus status,
        String bannerUrl,
        // Venue field flat mapped from venue
        String venueName,
        // Fields flat mapped from sessions
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        BigDecimal minPrice
) {}
