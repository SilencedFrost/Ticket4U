package com.ticket4u.eventmanagement.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record EventSessionRequest(
        String name,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate
) {}