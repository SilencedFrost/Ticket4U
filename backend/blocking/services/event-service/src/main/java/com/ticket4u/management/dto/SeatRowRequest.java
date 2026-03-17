package com.ticket4u.management.dto;

import java.math.BigDecimal;

public record SeatRowRequest(
        String prefix,
        int count,
        BigDecimal priceOverride
) {}