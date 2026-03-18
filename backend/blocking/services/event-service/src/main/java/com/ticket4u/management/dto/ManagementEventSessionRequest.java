package com.ticket4u.management.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ManagementEventSessionRequest(
        String name,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate
) {}