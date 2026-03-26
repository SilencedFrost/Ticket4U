package com.ticket4u.core.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ZoneResponse(
        UUID id,
        String name,
        Boolean isStanding,
        Integer capacity,
        Integer purchaseLimit,
        BigDecimal price,
        String descriptionVi,
        String descriptionEn,
        String giftImageUrl,
        List<String> perks,
        List<SeatResponse> seats
) {
}