package com.ticket4u.feature.eventdetail.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ShowtimeResponse(
         String id,
         OffsetDateTime date,
         OffsetDateTime time,
         List<SeatTypeResponse>seatTypes
) {}
