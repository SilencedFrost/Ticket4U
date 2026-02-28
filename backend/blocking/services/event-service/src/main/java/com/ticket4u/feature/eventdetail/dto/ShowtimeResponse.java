package com.ticket4u.feature.eventdetail.dto;

import java.util.List;

public record ShowtimeResponse(
         String id,
         String date,
         String time,
         List<SeatTypeResponse>seatTypes
) {}
