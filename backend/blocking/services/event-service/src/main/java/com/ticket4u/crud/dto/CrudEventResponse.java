package com.ticket4u.crud.dto;

import com.ticket4u.core.entity.Event;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CrudEventResponse(
        UUID id,
        String name,
        String addressLine,
        OffsetDateTime firstSessionStart,
        Event.EventStatus status,
        String bannerUrl,
        UUID venueId,
        String venueName,
        String eventLayout,
        String aboutVi,
        String aboutEn,
        String termsAndConditions,
        String policyRefund,
        String seatingPlanImageUrl,
        Integer categoryId,
        String categoryName,
        Integer ticketsSold,
        Integer totalCapacity,
        BigDecimal revenue,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}