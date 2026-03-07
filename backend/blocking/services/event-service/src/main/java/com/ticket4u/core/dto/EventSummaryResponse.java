package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Shared response DTO for a summarized event card (id, name, banner, dates, min price, category).
 *
 * Moved to core because multiple features use this exact shape:
 *   - feature.homepage  : event lists, featured, trending, filter results
 *   - feature.eventdetail : related events sidebar
 *
 * Rule: when a DTO is imported by more than one feature it no longer belongs
 * to any single feature — it belongs to core.
 */
public record EventSummaryResponse(
        UUID id,
        String name,
        String bannerUrl,
        String addressLine,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        BigDecimal minPrice,
        String categoryName
) {}
