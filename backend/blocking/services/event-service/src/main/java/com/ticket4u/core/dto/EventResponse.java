package com.ticket4u.core.dto;

import com.ticket4u.core.entity.BannerUrl;
import com.ticket4u.core.entity.Event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Detailed Event DTO
 * Core DTO used by homepage and event detail features
 * Use "Event.withAllEntities" entity graph
 */
public record EventResponse(
        UUID id,
        String name,
        UUID organizerId,
        Set<CategorySummaryResponse> categories,
        String addressLine,
        Event.EventStatus status,
        BannerUrl bannerUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String aboutVi,
        String aboutEn,
        String termsAndConditions,
        String policyRefund,
        String seatingPlanImageUrl,
        VenueSummaryResponse venue,
        BigDecimal longitude,
        BigDecimal latitude,
        Set<EventSessionResponse> sessions,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
