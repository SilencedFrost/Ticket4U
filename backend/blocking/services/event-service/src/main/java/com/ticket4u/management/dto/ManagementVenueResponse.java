package com.ticket4u.management.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ManagementVenueResponse(
        UUID id,
        String name,
        String addressLine,
        BigDecimal longitude,
        BigDecimal latitude,
        String imageUrl,
        String layout
) {}