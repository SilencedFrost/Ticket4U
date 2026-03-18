package com.ticket4u.management.dto;

import java.util.List;
import java.util.UUID;

public record ManagementEventLayoutRequest(
        boolean useVenueLayout,
        UUID venueId,           // required when switching back to venue layout
        String customLayoutJson,
        List<VenueZoneLink> venueZoneLinks
) {
    public record VenueZoneLink(
            String venueZoneName,
            UUID zoneId
    ) {}
}