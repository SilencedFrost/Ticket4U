package com.ticket4u.feature.event.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ZoneResponse(
                UUID id,
                String name,
                BigDecimal price,
                Integer available,
                String descriptionVi,
                String descriptionEn,
                String giftImageUrl,
                List<String> perks) {
}
