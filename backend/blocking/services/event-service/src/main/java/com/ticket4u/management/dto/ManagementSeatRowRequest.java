package com.ticket4u.management.dto;

import java.math.BigDecimal;

public record ManagementSeatRowRequest(
        String prefix,
        int count,
        BigDecimal priceOverride
) {}