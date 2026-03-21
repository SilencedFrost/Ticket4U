package com.ticket4u.dto;

import java.util.UUID;

public record SeatResponse(
        UUID seatId,
        String status
) {
}
