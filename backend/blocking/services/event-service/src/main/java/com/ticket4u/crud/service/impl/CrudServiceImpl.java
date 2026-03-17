package com.ticket4u.crud.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.core.entity.*;
import com.ticket4u.core.repository.CategoryRepository;
import com.ticket4u.crud.client.CrudProfileClient;
import com.ticket4u.crud.dto.*;
import com.ticket4u.crud.repository.*;
import com.ticket4u.crud.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrudServiceImpl implements CrudService {

    private final CrudEventRepository   eventRepository;
    private final CrudSessionRepository sessionRepository;
    private final CrudZoneRepository    zoneRepository;
    private final CrudSeatRepository    seatRepository;
    private final CrudVenueRepository   venueRepository;
    private final CategoryRepository    categoryRepository;
    private final CrudProfileClient     profileClient;
    private final ObjectMapper          objectMapper;

    // Profile

    @Override
    public CrudProfileResponse getProfile(UUID organizerId) {
        return profileClient.getOrganizerProfile(organizerId);
    }

    // Categories

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummaryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategorySummaryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    // Events

    @Override
    @Transactional(readOnly = true)
    public List<CrudEventResponse> getEvents(UUID organizerId) {
        return eventRepository.findAllByOrganizerIdOrderByFirstSessionStartDesc(organizerId)
                .stream()
                .map(e -> mapEventToResponse(e, true))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CrudEventResponse getEvent(UUID organizerId, UUID eventId) {
        return mapEventToResponse(findEvent(organizerId, eventId), true);
    }

    @Override
    @Transactional
    public CrudEventResponse createEvent(UUID organizerId, CrudEventRequest request) {
        Event event = new Event();
        event.setOrganizerId(organizerId);
        applyRequestToEvent(event, request);
        event.setStatus(request.status() != null
                ? Event.EventStatus.valueOf(request.status().toUpperCase())
                : Event.EventStatus.EDITING);
        Event saved = eventRepository.save(event);

        // Auto-create first session
        EventSession session = new EventSession();
        session.setEvent(saved);
        session.setName(request.name());
        session.setStartDate(request.startDate());
        session.setEndDate(request.endDate());
        session.setStatus(EventSession.SessionStatus.SCHEDULED);
        sessionRepository.save(session);

        return mapEventToResponse(saved, false);
    }

    @Override
    @Transactional
    public CrudEventResponse updateEvent(UUID organizerId, UUID eventId, CrudEventRequest request) {
        Event event = findEvent(organizerId, eventId);
        applyRequestToEvent(event, request);
        if (request.status() != null) {
            event.setStatus(Event.EventStatus.valueOf(request.status().toUpperCase()));
        }
        Event saved = eventRepository.save(event);

        // Update first session's dates
        List<EventSession> sessions = sessionRepository.findAllByEventIdOrderByStartDateAsc(eventId);
        if (!sessions.isEmpty()) {
            EventSession first = sessions.get(0);
            first.setStartDate(request.startDate());
            first.setEndDate(request.endDate());
            sessionRepository.save(first);
        }

        return mapEventToResponse(saved, false);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID organizerId, UUID eventId) {
        Event event = findEvent(organizerId, eventId);
        event.setStatus(Event.EventStatus.CANCELLED);
        event.setCancelledAt(OffsetDateTime.now());
        eventRepository.save(event);
    }

    // Sessions

    @Override
    @Transactional(readOnly = true)
    public List<EventSessionResponse> getSessions(UUID organizerId, UUID eventId) {
        findEvent(organizerId, eventId);
        return sessionRepository.findAllByEventIdOrderByStartDateAsc(eventId)
                .stream()
                .map(this::mapSessionToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventSessionResponse updateSession(UUID organizerId, UUID eventId, UUID sessionId,
                                              EventSessionRequest request) {
        EventSession session = sessionRepository
                .findByIdAndEventIdAndOrganizerId(sessionId, eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));

        if (request.name() != null && !request.name().isBlank()) {
            session.setName(request.name());
        }
        session.setStartDate(request.startDate());
        session.setEndDate(request.endDate());
        return mapSessionToResponse(sessionRepository.save(session));
    }

    // Zones

    @Override
    @Transactional(readOnly = true)
    public List<CrudZoneResponse> getZones(UUID organizerId, UUID sessionId) {
        findSession(organizerId, sessionId);
        return zoneRepository.findAllBySessionId(sessionId)
                .stream()
                .map(this::mapZoneToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CrudZoneResponse createZone(UUID organizerId, UUID sessionId, ZoneRequest request) {
        EventSession session = findSession(organizerId, sessionId);
        Zone zone = new Zone();
        zone.setSession(session);
        applyRequestToZone(zone, request);
        return mapZoneToResponse(zoneRepository.save(zone));
    }

    @Override
    @Transactional
    public CrudZoneResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId, ZoneRequest request) {
        findSession(organizerId, sessionId);
        Zone zone = zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
        applyRequestToZone(zone, request);
        return mapZoneToResponse(zoneRepository.save(zone));
    }

    @Override
    @Transactional
    public void deleteZone(UUID organizerId, UUID sessionId, UUID zoneId) {
        findSession(organizerId, sessionId);
        Zone zone = zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
        zoneRepository.delete(zone);
    }

    // Layout

    @Override
    @Transactional
    public EventLayoutResponse applyLayout(UUID organizerId, UUID eventId, EventLayoutRequest request) {
        Event event = findEvent(organizerId, eventId);

        String layoutJson;
        if (request.useVenueLayout()) {
            Venue venue = event.getVenue();
            if (venue == null)
                throw new IllegalStateException("Event has no venue assigned.");
            if (venue.getLayout() == null)
                throw new IllegalStateException("Venue has no layout defined.");
            layoutJson = venue.getLayout();
        } else {
            if (request.customLayoutJson() == null || request.customLayoutJson().isBlank())
                throw new IllegalArgumentException("customLayoutJson must be provided when useVenueLayout is false.");
            layoutJson = request.customLayoutJson();
        }

        event.setEventLayout(layoutJson);
        eventRepository.save(event);

        // Generate seats for all sessions from the layout
        List<EventSession> sessions = sessionRepository.findAllByEventIdOrderByStartDateAsc(eventId);
        for (EventSession session : sessions) {
            generateSeatsFromLayout(session, layoutJson);
        }

        return new EventLayoutResponse(eventId, layoutJson);
    }

    @Override
    @Transactional(readOnly = true)
    public EventLayoutResponse getLayout(UUID organizerId, UUID eventId) {
        Event event = findEvent(organizerId, eventId);
        String layoutJson = event.getEventLayout();
        if (layoutJson == null && event.getVenue() != null) {
            layoutJson = event.getVenue().getLayout();
        }
        return new EventLayoutResponse(eventId, layoutJson);
    }

    // Seats

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeats(UUID organizerId, UUID sessionId, UUID zoneId) {
        Zone zone = resolveZone(organizerId, sessionId, zoneId);
        return seatRepository.findAllByZoneIdOrderByRowNameAscColNameAsc(zone.getId())
                .stream().map(this::mapSeatToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SeatResponse> generateSeats(UUID organizerId, UUID sessionId, UUID zoneId,
                                            SeatGenerateRequest request) {
        Zone zone = resolveZone(organizerId, sessionId, zoneId);
        seatRepository.deleteAllByZoneId(zone.getId());

        List<Seat> seats = new ArrayList<>();
        for (SeatRowRequest row : request.rows()) {
            String prefix = row.prefix() != null ? row.prefix() : "A";
            for (int col = 1; col <= row.count(); col++) {
                String colStr   = String.valueOf(col);
                String seatCode = prefix + "-" + col;
                if (seatCode.length() > 20) seatCode = seatCode.substring(0, 20);

                Seat seat = new Seat();
                seat.setZone(zone);
                seat.setName(prefix + col);
                seat.setRowName(prefix.length() > 5 ? prefix.substring(0, 5) : prefix);
                seat.setColName(colStr.length() > 5 ? colStr.substring(0, 5) : colStr);
                seat.setSeatCode(seatCode);
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                seat.setPriceOverride(row.priceOverride());
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        return seats.stream().map(this::mapSeatToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeatResponse updateSeatPrice(UUID organizerId, UUID sessionId, UUID zoneId, UUID seatId,
                                        SeatPriceOverrideRequest request) {
        Zone zone = resolveZone(organizerId, sessionId, zoneId);
        Seat seat = seatRepository.findByIdAndZoneId(seatId, zone.getId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found: " + seatId));
        seat.setPriceOverride(request.priceOverride());
        return mapSeatToResponse(seatRepository.save(seat));
    }

    @Override
    @Transactional
    public void deleteSeats(UUID organizerId, UUID sessionId, UUID zoneId) {
        Zone zone = resolveZone(organizerId, sessionId, zoneId);
        seatRepository.deleteAllByZoneId(zone.getId());
    }

    // Layout seat generation
    //
    // Handles two JSON formats:
    //
    // 1. Venue default layout (flat):
    //    { "zones": [ { "zone_name": "VIP", "seats": [...] } ] }
    //
    // 2. Custom multi-floor layout:
    //    { "floors": [ { "zones": [ { "zone_name": "VIP", "seats": [...] } ] } ] }
    //
    // In both cases: seat_code = seat_id from JSON (stable bridge to seats table)

    private void generateSeatsFromLayout(EventSession session, String layoutJson) {
        try {
            JsonNode root = objectMapper.readTree(layoutJson);
            List<Zone> sessionZones = zoneRepository.findAllBySessionId(session.getId());

            // Collect all zone nodes regardless of format
            List<JsonNode> zoneNodes = new ArrayList<>();

            if (root.has("floors") && root.path("floors").isArray()) {
                // Custom multi-floor layout — extract zones from each floor
                for (JsonNode floor : root.path("floors")) {
                    JsonNode zones = floor.path("zones");
                    if (zones.isArray()) {
                        for (JsonNode z : zones) zoneNodes.add(z);
                    }
                }
            } else if (root.has("zones") && root.path("zones").isArray()) {
                // Venue default layout — zones at root level
                for (JsonNode z : root.path("zones")) zoneNodes.add(z);
            }

            if (zoneNodes.isEmpty()) return;

            for (JsonNode zoneNode : zoneNodes) {
                String   zoneName = zoneNode.path("zone_name").asText();
                JsonNode seats    = zoneNode.path("seats");
                if (!seats.isArray() || seats.isEmpty()) continue;

                Zone matchedZone = sessionZones.stream()
                        .filter(z -> z.getName().equalsIgnoreCase(zoneName))
                        .findFirst().orElse(null);

                if (matchedZone == null) {
                    log.warn("Layout zone '{}' has no matching Zone in session {}", zoneName, session.getId());
                    continue;
                }

                seatRepository.deleteAllByZoneId(matchedZone.getId());

                List<Seat> newSeats = new ArrayList<>();
                for (JsonNode seatNode : seats) {
                    String seatId   = seatNode.path("seat_id").asText();
                    String seatName = seatNode.path("seat_name").asText();

                    Seat seat = new Seat();
                    seat.setZone(matchedZone);
                    seat.setName(seatName.length() > 255 ? seatName.substring(0, 255) : seatName);
                    seat.setSeatCode(seatId.length() > 20 ? seatId.substring(0, 20) : seatId);
                    seat.setStatus(Seat.SeatStatus.AVAILABLE);
                    newSeats.add(seat);
                }
                seatRepository.saveAll(newSeats);
                log.info("Generated {} seats for zone '{}' in session {}", newSeats.size(), zoneName, session.getId());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse layout JSON: " + e.getMessage(), e);
        }
    }

    // Private Helpers

    private Event findEvent(UUID organizerId, UUID eventId) {
        return eventRepository.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
    }

    private EventSession findSession(UUID organizerId, UUID sessionId) {
        return sessionRepository.findByIdAndOrganizerId(sessionId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
    }

    private Zone resolveZone(UUID organizerId, UUID sessionId, UUID zoneId) {
        findSession(organizerId, sessionId);
        return zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
    }

    private void applyRequestToEvent(Event event, CrudEventRequest req) {
        event.setName(req.name());
        event.setAddressLine(req.addressLine());
        event.setBannerUrl(req.bannerUrl() != null ? req.bannerUrl() : "");

        if (req.venueId() != null) {
            Venue venue = venueRepository.findById(req.venueId())
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found: " + req.venueId()));
            event.setVenue(venue);
        }
        if (req.categoryId() != null) {
            Category cat = new Category();
            cat.setId(req.categoryId());
            event.setCategory(cat);
        }
        if (req.aboutVi() != null)             event.setAboutVi(req.aboutVi());
        if (req.aboutEn() != null)             event.setAboutEn(req.aboutEn());
        if (req.termsAndConditions() != null)  event.setTermsAndConditions(req.termsAndConditions());
        if (req.policyRefund() != null)        event.setPolicyRefund(req.policyRefund());
        if (req.seatingPlanImageUrl() != null) event.setSeatingPlanImageUrl(req.seatingPlanImageUrl());
    }

    private void applyRequestToZone(Zone zone, ZoneRequest req) {
        zone.setName(req.name());
        zone.setIsStanding(req.isStanding());
        zone.setCapacity(req.capacity());
        zone.setPrice(req.price());
        zone.setPurchaseLimit(req.purchaseLimit());
        zone.setDescriptionVi(req.descriptionVi());
        zone.setDescriptionEn(req.descriptionEn());
        zone.setGiftImageUrl(req.giftImageUrl());
        zone.setPerks(req.perks());
        if (zone.getQuantitySold() == null) zone.setQuantitySold(0);
    }

    private CrudEventResponse mapEventToResponse(Event event, boolean includeStats) {
        Integer ticketsSold   = includeStats ? eventRepository.sumTicketsSoldByEventId(event.getId()) : 0;
        Integer totalCapacity = includeStats ? eventRepository.sumCapacityByEventId(event.getId()) : 0;
        BigDecimal revenue    = includeStats ? eventRepository.sumRevenueByEventId(event.getId()) : BigDecimal.ZERO;

        OffsetDateTime firstSessionStart = event.getSessions() == null ? null :
                event.getSessions().stream()
                        .map(EventSession::getStartDate)
                        .filter(d -> d != null)
                        .min(OffsetDateTime::compareTo)
                        .orElse(null);

        return new CrudEventResponse(
                event.getId(),
                event.getName(),
                event.getAddressLine(),
                firstSessionStart,
                event.getStatus(),
                event.getBannerUrl(),
                event.getVenue() != null ? event.getVenue().getId() : null,
                event.getVenue() != null ? event.getVenue().getName() : null,
                event.getEventLayout(),
                event.getAboutVi(),
                event.getAboutEn(),
                event.getTermsAndConditions(),
                event.getPolicyRefund(),
                event.getSeatingPlanImageUrl(),
                event.getCategory() != null ? event.getCategory().getId() : null,
                event.getCategory() != null ? event.getCategory().getName() : null,
                ticketsSold,
                totalCapacity,
                revenue,
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    private EventSessionResponse mapSessionToResponse(EventSession s) {
        return new EventSessionResponse(
                s.getId().toString(),
                s.getStartDate(),
                s.getEndDate(),
                s.getStatus(),
                s.getName(),
                null
        );
    }

    private CrudZoneResponse mapZoneToResponse(Zone zone) {
        int seatCount = Boolean.TRUE.equals(zone.getIsStanding()) ? 0
                : seatRepository.countByZoneId(zone.getId());
        return new CrudZoneResponse(
                zone.getId(),
                zone.getSession() != null ? zone.getSession().getId() : null,
                zone.getName(),
                zone.getIsStanding(),
                zone.getCapacity(),
                zone.getQuantitySold(),
                zone.getPurchaseLimit(),
                zone.getPrice(),
                zone.getDescriptionVi(),
                zone.getDescriptionEn(),
                zone.getGiftImageUrl(),
                zone.getPerks(),
                seatCount
        );
    }

    private SeatResponse mapSeatToResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getZone().getId(),
                seat.getName(),
                seat.getRowName(),
                seat.getColName(),
                seat.getSeatCode(),
                seat.getStatus() != null ? seat.getStatus().name() : null,
                seat.getPriceOverride()
        );
    }
}