package com.ticket4u.dto;

import java.util.List;
import java.util.UUID;

public record ZoneResponse(
        UUID zoneId,
        String zoneName,
        List<SeatResponse> seats,
        Long takenCount
) {
}
