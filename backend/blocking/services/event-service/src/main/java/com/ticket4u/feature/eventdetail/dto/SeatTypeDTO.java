package com.ticket4u.feature.eventdetail.dto;

import java.util.List;
import java.util.UUID;

public record SeatTypeDTO (
         UUID id,
         String name,
         String price,
         Integer available,
         String description,
         String image,
         List<String>benefits
) {}
