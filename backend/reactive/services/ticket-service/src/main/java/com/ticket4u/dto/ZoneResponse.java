package com.ticket4u.dto;

import java.util.List;

public record ZoneResponse(
        String zoneId,
        String zoneName,
        List<SeatResponse> seats,
        String status
) {
}
