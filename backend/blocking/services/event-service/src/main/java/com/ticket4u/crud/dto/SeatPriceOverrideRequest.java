package com.ticket4u.crud.dto;

import java.math.BigDecimal;

public record SeatPriceOverrideRequest(
        BigDecimal priceOverride
) {}