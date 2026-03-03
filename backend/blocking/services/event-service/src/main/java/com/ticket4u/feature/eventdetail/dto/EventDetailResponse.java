package com.ticket4u.feature.eventdetail.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventDetailResponse (
         UUID id,
         String name,
         OffsetDateTime startDate,
         String addressLine,
         String description,
         String minPrice,
         String maxPrice,
         Integer categoryId,
         String bannerUrl,
         String seatingPlanImageUrl,
         UUID organizerId,
         List<ShowtimeResponse> showtimes
) {}
