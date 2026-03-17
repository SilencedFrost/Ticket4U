package com.ticket4u.management.dto;

import java.math.BigDecimal;

public record SeatPriceOverrideRequest(
        BigDecimal priceOverride
) {}