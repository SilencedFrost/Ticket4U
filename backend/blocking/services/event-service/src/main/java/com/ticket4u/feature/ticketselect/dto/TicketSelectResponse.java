package com.ticket4u.feature.ticketselect.dto;

import java.util.List;
import java.util.UUID;

public record TicketSelectResponse(
        UUID eventId,
        String name,
        String startDate,
        String endDate,
        String addressLine,
        List<ZoneDTO> zones,
        List<FloorDTO> floors
) {}