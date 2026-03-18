package com.ticket4u.management.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ManagementZoneResponse(
        UUID id,
        UUID sessionId,
        String name,
        Boolean isStanding,
        Integer capacity,
        Integer quantitySold,
        Integer purchaseLimit,
        BigDecimal price,
        String descriptionVi,
        String descriptionEn,
        String giftImageUrl,
        List<String> perks,
        int seatCount,
        Integer gridRows,   // derived from seat data — null if no seats yet
        Integer gridCols    // max seats per row
) {}