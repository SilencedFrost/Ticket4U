package com.ticket4u.eventmanagement.dto;

import java.math.BigDecimal;

public record SeatPriceOverrideRequest(
        BigDecimal priceOverride
) {}