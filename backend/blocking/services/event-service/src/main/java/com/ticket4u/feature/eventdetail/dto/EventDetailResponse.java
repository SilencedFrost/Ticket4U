package com.ticket4u.feature.eventdetail.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventDetailResponse (
         UUID eventId,
         String eventTitle,
         OffsetDateTime startDate,
         String address,
         String description,
         String minPrice,
         String maxPrice,
         Integer categoryId,
         ImageEventResponse imgEvent,
         OrganizerResponse organizer,
         List<ShowtimeResponse> showtimes
) {}
