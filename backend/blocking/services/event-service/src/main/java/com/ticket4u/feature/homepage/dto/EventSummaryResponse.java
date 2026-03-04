package com.ticket4u.feature.homepage.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

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
