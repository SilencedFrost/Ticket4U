package com.ticket4u.feature.ticketselect.dto;

import java.util.UUID;

public record FloorDTO(
        UUID id,
        String floorName,
        int floorOrder,
        String layoutJson,        // raw JSON from venue_layouts
        String modifications      // raw JSON from event_layout_modifications
) {}