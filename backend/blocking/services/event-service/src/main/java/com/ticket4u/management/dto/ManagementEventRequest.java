package com.ticket4u.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ManagementEventRequest(
        @NotBlank String name,
        UUID venueId,
        Integer categoryId,
        @NotBlank String addressLine,
        String bannerUrl,
        String status,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate,
        String aboutVi,
        String aboutEn,
        String termsAndConditions,
        String policyRefund,
        String seatingPlanImageUrl
) {}

