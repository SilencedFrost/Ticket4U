package com.ticket4u.management.service;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.management.dto.*;

import java.util.List;
import java.util.UUID;

public interface ManagementService {

    // Profile
    OrganizerProfileResponse getProfile(UUID organizerId);

    // Categories
    List<CategorySummaryResponse> getCategories();

    // Events
    List<ManagementEventResponse> getEvents(UUID organizerId);
    ManagementEventResponse getEvent(UUID organizerId, UUID eventId);
    ManagementEventResponse createEvent(UUID organizerId, ManagementEventRequest request);
    ManagementEventResponse updateEvent(UUID organizerId, UUID eventId, ManagementEventRequest request);
    void deleteEvent(UUID organizerId, UUID eventId);

    // Sessions
    List<EventSessionResponse> getSessions(UUID organizerId, UUID eventId);
    EventSessionResponse updateSession(UUID organizerId, UUID eventId, UUID sessionId, EventSessionRequest request);

    // Zones
    List<ManagementZoneResponse> getZones(UUID organizerId, UUID sessionId);
    ManagementZoneResponse createZone(UUID organizerId, UUID sessionId, ZoneRequest request);
    ManagementZoneResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId, ZoneRequest request);
    void deleteZone(UUID organizerId, UUID sessionId, UUID zoneId);

    // Layout
    EventLayoutResponse getLayout(UUID organizerId, UUID eventId);
    EventLayoutResponse applyLayout(UUID organizerId, UUID eventId, EventLayoutRequest request);

    // Seats
    List<SeatResponse> getSeats(UUID organizerId, UUID sessionId, UUID zoneId);
    List<SeatResponse> generateSeats(UUID organizerId, UUID sessionId, UUID zoneId, SeatGenerateRequest request);
    SeatResponse updateSeatPrice(UUID organizerId, UUID sessionId, UUID zoneId, UUID seatId, SeatPriceOverrideRequest request);
    void deleteSeats(UUID organizerId, UUID sessionId, UUID zoneId);
}