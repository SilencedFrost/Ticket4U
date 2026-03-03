package com.ticket4u.feature.eventdetail.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record SeatTypeResponse(
         UUID id,
         String name,
         BigDecimal price,
         Integer available,
         String description,
         String image,
         List<String>benefits
) {}
