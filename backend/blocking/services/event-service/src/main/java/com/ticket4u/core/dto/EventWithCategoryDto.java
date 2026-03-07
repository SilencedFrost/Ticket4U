package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventWithCategoryDto(
        UUID id,
        String name,
        String bannerUrl,
        String addressLine,
        Instant startDate,
        Instant endDate,
        BigDecimal minPrice,
        String categoryName
) {}
