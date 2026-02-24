package com.ticket4u.feature.ticketselect.dto;

import java.util.UUID;

public record ZoneDTO(
        UUID id,
        String name,
        double price,
        int capacity,
        int quantitySold,
        Integer purchaseLimit
) {}