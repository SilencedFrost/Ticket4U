package com.ticket4u.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record ZoneRequest(
        @NotBlank String name,
        @NotNull  Boolean isStanding,
        Integer capacity,
        @NotNull BigDecimal price,
        Integer purchaseLimit,
        String descriptionVi,
        String descriptionEn,
        String giftImageUrl,
        List<String> perks
) {}
