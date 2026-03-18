package com.ticket4u.management.service.impl;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.core.entity.*;
import com.ticket4u.core.repository.CategoryRepository;
import com.ticket4u.management.dto.*;
import com.ticket4u.management.repository.*;
import com.ticket4u.management.service.ManagementService;
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
public class ManagementServiceImpl implements ManagementService {

    private final EventManagementRepository        eventRepository;
    private final EventManagementSessionRepository sessionRepository;
    private final EventManagementZoneRepository    zoneRepository;
    private final EventManagementSeatRepository    seatRepository;
    private final EventManagementVenueRepository   venueRepository;
    private final CategoryRepository               categoryRepository;
    private final ObjectMapper                     objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummaryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategorySummaryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagementEventResponse> getEvents(UUID organizerId) {
        return eventRepository.findAllByOrganizerIdOrderByFirstSessionStartDesc(organizerId)
                .stream().map(e -> mapEventToResponse(e, true)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ManagementEventResponse getEvent(UUID organizerId, UUID eventId) {
        return mapEventToResponse(findEvent(organizerId, eventId), true);
    }

    @Override
    @Transactional
    public ManagementEventResponse createEvent(UUID organizerId, ManagementEventRequest request) {
        Event event = new Event();
        event.setOrganizerId(organizerId);
        applyRequestToEvent(event, request);
        event.setStatus(request.status() != null
                ? Event.EventStatus.valueOf(request.status().toUpperCase())
                : Event.EventStatus.EDITING);
        Event saved = eventRepository.save(event);

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
    public ManagementEventResponse updateEvent(UUID organizerId, UUID eventId, ManagementEventRequest request) {
        Event event = findEvent(organizerId, eventId);
        applyRequestToEvent(event, request);
        if (request.status() != null)
            event.setStatus(Event.EventStatus.valueOf(request.status().toUpperCase()));
        Event saved = eventRepository.save(event);

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

    @Override
    @Transactional(readOnly = true)
    public List<EventSessionResponse> getSessions(UUID organizerId, UUID eventId) {
        findEvent(organizerId, eventId);
        return sessionRepository.findAllByEventIdOrderByStartDateAsc(eventId)
                .stream().map(this::mapSessionToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventSessionResponse updateSession(UUID organizerId, UUID eventId, UUID sessionId,
                                              EventSessionRequest request) {
        EventSession session = sessionRepository
                .findByIdAndEventIdAndOrganizerId(sessionId, eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
        if (request.name() != null && !request.name().isBlank()) session.setName(request.name());
        session.setStartDate(request.startDate());
        session.setEndDate(request.endDate());
        return mapSessionToResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagementZoneResponse> getZones(UUID organizerId, UUID sessionId) {
        findSession(organizerId, sessionId);
        return zoneRepository.findAllBySessionId(sessionId)
                .stream().map(this::mapZoneToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ManagementZoneResponse createZone(UUID organizerId, UUID sessionId, ZoneRequest request) {
        EventSession session = findSession(organizerId, sessionId);
        Zone zone = new Zone();
        zone.setSession(session);
        applyRequestToZone(zone, request);
        Zone saved = zoneRepository.save(zone);
        // Auto-generate seats from grid if specified
        if (!Boolean.TRUE.equals(request.isStanding())
                && request.gridRows() != null && request.gridRows() > 0
                && request.gridCols() != null && request.gridCols() > 0) {
            generateSeatsFromGrid(saved, request.gridRows(), request.gridCols());
        }
        return mapZoneToResponse(saved);
    }

    @Override
    @Transactional
    public ManagementZoneResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId, ZoneRequest request) {
        findSession(organizerId, sessionId);
        Zone zone = zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
        applyRequestToZone(zone, request);
        Zone saved = zoneRepository.save(zone);
        // Regenerate seats if grid dimensions provided
        if (!Boolean.TRUE.equals(request.isStanding())
                && request.gridRows() != null && request.gridRows() > 0
                && request.gridCols() != null && request.gridCols() > 0) {
            seatRepository.deleteAllByZoneId(saved.getId());
            generateSeatsFromGrid(saved, request.gridRows(), request.gridCols());
        }
        return mapZoneToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteZone(UUID organizerId, UUID sessionId, UUID zoneId) {
        findSession(organizerId, sessionId);
        Zone zone = zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
        zoneRepository.delete(zone);
    }

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

        if (request.useVenueLayout()) {
            // Venue layout — clear custom layout, keep venue association
            event.setLayout(null);
        } else {
            // Custom layout — store layout JSON and clear venue so ticket-select
            // uses event.layout directly, not venue.layout
            event.setLayout(layoutJson);
            event.setVenue(null);
        }
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
        // Rule:
        // - event.layout != null  → custom layout, use it directly
        // - event.layout == null && venue != null → use venue default layout
        // - both null → no layout configured
        String layoutJson = event.getLayout();
        if (layoutJson == null && event.getVenue() != null) {
            layoutJson = event.getVenue().getLayout();
        }
        return new EventLayoutResponse(eventId, layoutJson);
    }

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
                String seatCode = prefix + col; // A1, A2, B1 — matches seed data pattern
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

    // ── Seat generation from grid dimensions ──────────────
    // Generates rows A-Z with gridCols seats each: A1, A2... B1, B2...
    private void generateSeatsFromGrid(Zone zone, int gridRows, int gridCols) {
        List<Seat> seats = new ArrayList<>();
        for (int r = 0; r < gridRows; r++) {
            String rowPrefix = r < 26
                    ? String.valueOf((char) ('A' + r))
                    : String.valueOf((char) ('A' + r / 26 - 1)) + (char) ('A' + r % 26);
            for (int c = 1; c <= gridCols; c++) {
                String seatCode = rowPrefix + c;
                if (seatCode.length() > 20) seatCode = seatCode.substring(0, 20);
                Seat seat = new Seat();
                seat.setZone(zone);
                seat.setName(rowPrefix + c);
                seat.setRowName(rowPrefix.length() > 5 ? rowPrefix.substring(0, 5) : rowPrefix);
                seat.setColName(String.valueOf(c).length() > 5 ? String.valueOf(c).substring(0, 5) : String.valueOf(c));
                seat.setSeatCode(seatCode);
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        log.info("Generated {}x{} = {} seats for zone '{}'", gridRows, gridCols, seats.size(), zone.getName());
    }

    // ── Seat generation from layout JSON ───────────────────
    // Handles two formats:
    // 1. Venue default: { "zones": [ { "zone_name": "VIP", "seats": [...] } ] }
    // 2. Custom multi-floor: { "floors": [ { "zones": [ { "zone_name": "VIP", "seats": [...] } ] } ] }
    private void generateSeatsFromLayout(EventSession session, String layoutJson) {
        try {
            JsonNode root = objectMapper.readTree(layoutJson);
            List<Zone> sessionZones = zoneRepository.findAllBySessionId(session.getId());

            List<JsonNode> zoneNodes = new ArrayList<>();
            if (root.has("floors") && root.path("floors").isArray()) {
                for (JsonNode floor : root.path("floors")) {
                    JsonNode zones = floor.path("zones");
                    if (zones.isArray()) for (JsonNode z : zones) zoneNodes.add(z);
                }
            } else if (root.has("zones") && root.path("zones").isArray()) {
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

    // ── Private helpers ────────────────────────────────────

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

    private void applyRequestToEvent(Event event, ManagementEventRequest req) {
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
        // Auto-calculate capacity from grid if both dimensions provided
        if (!Boolean.TRUE.equals(req.isStanding())
                && req.gridRows() != null && req.gridRows() > 0
                && req.gridCols() != null && req.gridCols() > 0) {
            zone.setCapacity(req.gridRows() * req.gridCols());
        } else {
            zone.setCapacity(req.capacity());
        }
        zone.setPrice(req.price());
        zone.setPurchaseLimit(req.purchaseLimit());
        zone.setDescriptionVi(req.descriptionVi());
        zone.setDescriptionEn(req.descriptionEn());
        zone.setGiftImageUrl(req.giftImageUrl());
        zone.setPerks(req.perks());
        if (zone.getQuantitySold() == null) zone.setQuantitySold(0);
    }

    private ManagementEventResponse mapEventToResponse(Event event, boolean includeStats) {
        Integer    ticketsSold   = includeStats ? eventRepository.sumTicketsSoldByEventId(event.getId()) : 0;
        Integer    totalCapacity = includeStats ? eventRepository.sumCapacityByEventId(event.getId()) : 0;
        BigDecimal revenue       = includeStats ? eventRepository.sumRevenueByEventId(event.getId()) : BigDecimal.ZERO;

        List<EventSession> sessionList = sessionRepository.findAllByEventIdOrderByStartDateAsc(event.getId());

        OffsetDateTime firstSessionStart = sessionList.stream()
                .map(EventSession::getStartDate)
                .filter(d -> d != null)
                .min(OffsetDateTime::compareTo)
                .orElse(null);

        List<ManagementEventResponse.SessionSummary> sessions = sessionList.stream()
                .map(s -> new ManagementEventResponse.SessionSummary(s.getId(), s.getStartDate(), s.getEndDate()))
                .collect(Collectors.toList());

        return new ManagementEventResponse(
                event.getId(),
                event.getName(),
                event.getAddressLine(),
                firstSessionStart,
                event.getStatus(),
                event.getBannerUrl(),
                event.getVenue() != null ? event.getVenue().getId() : null,
                event.getVenue() != null ? event.getVenue().getName() : null,
                event.getLayout(),
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
                event.getUpdatedAt(),
                sessions
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

    private ManagementZoneResponse mapZoneToResponse(Zone zone) {
        if (Boolean.TRUE.equals(zone.getIsStanding())) {
            return new ManagementZoneResponse(
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
                    0, null, null
            );
        }
        int seatCount = seatRepository.countByZoneId(zone.getId());
        // Derive grid dimensions from actual seat data
        Integer gridCols = seatRepository.maxColNumberByZoneId(zone.getId());
        Integer gridRows = (gridCols != null && gridCols > 0)
                ? (int) Math.ceil((double) seatCount / gridCols) : null;
        return new ManagementZoneResponse(
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
                seatCount, gridRows, gridCols
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