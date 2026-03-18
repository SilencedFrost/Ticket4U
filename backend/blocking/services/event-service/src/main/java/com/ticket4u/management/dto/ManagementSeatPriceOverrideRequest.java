package com.ticket4u.management.dto;

import java.math.BigDecimal;

public record ManagementSeatPriceOverrideRequest(
        BigDecimal priceOverride
) {}