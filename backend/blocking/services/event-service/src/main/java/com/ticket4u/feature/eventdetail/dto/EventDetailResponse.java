package com.ticket4u.feature.eventdetail.dto;

import java.util.List;
import java.util.UUID;

public record EventDetailResponse (
         UUID eventId,
         String eventTitle,
         String date,
         String time,
         String address,
         String description,
         String minPrice,
         String maxPrice,
         Integer categoryId,
         ImageEventResponse imgEvent,
         OrganizerResponse organizer,
         List<ShowtimeResponse> showtimes
) {}
