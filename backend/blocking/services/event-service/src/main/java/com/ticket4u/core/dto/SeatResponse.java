package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SeatResponse(
        UUID id,
        String name,
        String rowName,
        String colName,
        String seatCode,
        BigDecimal priceOverride
) {}
