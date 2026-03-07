package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CategoryWithEventDto(
        Integer categoryId,
        String categoryName,
        UUID eventId,
        String eventName,
        String bannerUrl,
        String addressLine,
        Instant startDate,
        Instant endDate,
        BigDecimal minPrice
) {}
