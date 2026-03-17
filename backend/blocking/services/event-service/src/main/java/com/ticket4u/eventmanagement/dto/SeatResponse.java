package com.ticket4u.eventmanagement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SeatResponse(
        UUID id,
        UUID zoneId,
        String name,
        String rowName,
        String colName,
        String seatCode,
        String status,
        BigDecimal priceOverride
) {}
