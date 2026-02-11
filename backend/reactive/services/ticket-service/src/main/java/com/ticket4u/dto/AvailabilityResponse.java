package com.ticket4u.dto;

import java.util.List;
import java.util.UUID;

public record AvailabilityResponse(
        UUID eventId,
        List<ZoneResponse> zones
) {
}
