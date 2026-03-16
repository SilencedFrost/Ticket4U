package com.ticket4u.crud.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record EventSessionRequest(
        String name,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate
) {}