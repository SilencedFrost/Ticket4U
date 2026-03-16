package com.ticket4u.crud.dto;

public record EventLayoutRequest(
        // true  → copy venue.layout into event.event_layout, then generate seats
        // false → use customLayoutJson provided below
        boolean useVenueLayout,
        String customLayoutJson
) {}