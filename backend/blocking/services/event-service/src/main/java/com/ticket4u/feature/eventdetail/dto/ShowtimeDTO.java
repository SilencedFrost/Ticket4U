package com.ticket4u.feature.eventdetail.dto;

import java.util.List;

public record ShowtimeDTO(
         String id,
         String date,
         String time,
         List<SeatTypeDTO>seatTypes
) {}
