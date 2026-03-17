package com.ticket4u.organizer.dto;

import java.math.BigDecimal;

public record SeatRowRequest(
        String prefix,
        int count,
        BigDecimal priceOverride
) {}