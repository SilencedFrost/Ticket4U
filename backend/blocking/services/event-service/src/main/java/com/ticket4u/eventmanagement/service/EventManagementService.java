package com.ticket4u.eventmanagement.service;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.eventmanagement.dto.*;

import java.util.List;
import java.util.UUID;

public interface EventManagementService {

    // Profile
    OrganizerProfileResponse getProfile(UUID organizerId);

    // Categories — reuses core.dto.CategorySummaryResponse
    List<CategorySummaryResponse> getCategories();

    // Events
    List<OrganizerEventResponse> getEvents(UUID organizerId);
    OrganizerEventResponse getEvent(UUID organizerId, UUID eventId);
    OrganizerEventResponse createEvent(UUID organizerId, OrganizerEventRequest request);
    OrganizerEventResponse updateEvent(UUID organizerId, UUID eventId, OrganizerEventRequest request);
    void deleteEvent(UUID organizerId, UUID eventId);

    // Sessions — reuses core.dto.EventSessionResponse
    List<EventSessionResponse> getSessions(UUID organizerId, UUID eventId);
    EventSessionResponse updateSession(UUID organizerId, UUID eventId, UUID sessionId, EventSessionRequest request);

    // Zones
    List<ZoneManagementResponse> getZones(UUID organizerId, UUID sessionId);
    ZoneManagementResponse createZone(UUID organizerId, UUID sessionId, ZoneRequest request);
    ZoneManagementResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId, ZoneRequest request);
    void deleteZone(UUID organizerId, UUID sessionId, UUID zoneId);

    // Layout
    EventLayoutResponse applyLayout(UUID organizerId, UUID eventId, EventLayoutRequest request);
    EventLayoutResponse getLayout(UUID organizerId, UUID eventId);

    // Seats
    List<SeatResponse> getSeats(UUID organizerId, UUID sessionId, UUID zoneId);
    List<SeatResponse> generateSeats(UUID organizerId, UUID sessionId, UUID zoneId, SeatGenerateRequest request);
    SeatResponse updateSeatPrice(UUID organizerId, UUID sessionId, UUID zoneId, UUID seatId, SeatPriceOverrideRequest request);
    void deleteSeats(UUID organizerId, UUID sessionId, UUID zoneId);
}