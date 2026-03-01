package com.ticket4u.feature.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCardResponse {
    private UUID id;
    private String name;
    private String bannerUrl;
    private String addressLine;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private BigDecimal minPrice;
    private String categoryName;
    private String status;
}

