package com.ticket4u.crud.service;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.crud.dto.*;

import java.util.List;
import java.util.UUID;

public interface CrudService {

    // Profile
    CrudProfileResponse getProfile(UUID organizerId);

    // Categories — reuses core.dto.CategorySummaryResponse
    List<CategorySummaryResponse> getCategories();

    // Events
    List<CrudEventResponse> getEvents(UUID organizerId);
    CrudEventResponse getEvent(UUID organizerId, UUID eventId);
    CrudEventResponse createEvent(UUID organizerId, CrudEventRequest request);
    CrudEventResponse updateEvent(UUID organizerId, UUID eventId, CrudEventRequest request);
    void deleteEvent(UUID organizerId, UUID eventId);

    // Sessions — reuses core.dto.EventSessionResponse
    List<EventSessionResponse> getSessions(UUID organizerId, UUID eventId);
    EventSessionResponse updateSession(UUID organizerId, UUID eventId, UUID sessionId, EventSessionRequest request);

    // Zones
    List<CrudZoneResponse> getZones(UUID organizerId, UUID sessionId);
    CrudZoneResponse createZone(UUID organizerId, UUID sessionId, ZoneRequest request);
    CrudZoneResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId, ZoneRequest request);
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