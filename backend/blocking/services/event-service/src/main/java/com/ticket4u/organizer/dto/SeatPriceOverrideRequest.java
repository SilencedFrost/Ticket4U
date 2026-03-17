package com.ticket4u.organizer.dto;

import java.math.BigDecimal;

public record SeatPriceOverrideRequest(
        BigDecimal priceOverride
) {}