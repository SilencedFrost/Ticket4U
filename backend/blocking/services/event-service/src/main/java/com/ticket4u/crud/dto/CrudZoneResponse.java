package com.ticket4u.crud.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CrudZoneResponse(
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
        int seatCount
) {}