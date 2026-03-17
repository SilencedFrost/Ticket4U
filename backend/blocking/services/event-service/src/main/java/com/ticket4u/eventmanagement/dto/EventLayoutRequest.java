package com.ticket4u.eventmanagement.dto;

public record EventLayoutRequest(
        // true  → copy venue.layout into event.layout, then generate seats
        // false → use customLayoutJson provided below
        boolean useVenueLayout,
        String customLayoutJson
) {}