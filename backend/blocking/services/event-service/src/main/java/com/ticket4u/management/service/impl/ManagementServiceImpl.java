package com.ticket4u.management.service.impl;

import com.ticket4u.core.dto.EventLayoutResponse;
import com.ticket4u.core.dto.SeatResponse;
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

    // ── Categories ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummaryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategorySummaryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    // ── Events ─────────────────────────────────────────────

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

    // ── Sessions ───────────────────────────────────────────

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
                                              ManagementEventSessionRequest request) {
        EventSession session = sessionRepository
                .findByIdAndEventIdAndOrganizerId(sessionId, eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
        if (request.name() != null && !request.name().isBlank()) session.setName(request.name());
        session.setStartDate(request.startDate());
        session.setEndDate(request.endDate());
        return mapSessionToResponse(sessionRepository.save(session));
    }

    // ── Zones ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ManagementZoneResponse> getZones(UUID organizerId, UUID sessionId) {
        findSession(organizerId, sessionId);
        return zoneRepository.findAllBySessionId(sessionId)
                .stream().map(this::mapZoneToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ManagementZoneResponse createZone(UUID organizerId, UUID sessionId, ManagementZoneRequest request) {
        EventSession session = findSession(organizerId, sessionId);
        Zone zone = new Zone();
        zone.setSession(session);
        applyRequestToZone(zone, request);
        Zone saved = zoneRepository.save(zone);
        if (!Boolean.TRUE.equals(request.isStanding())
                && request.gridRows() != null && request.gridRows() > 0
                && request.gridCols() != null && request.gridCols() > 0) {
            generateSeatsFromGrid(saved, request.gridRows(), request.gridCols());
        }
        return mapZoneToResponse(saved);
    }

    @Override
    @Transactional
    public ManagementZoneResponse updateZone(UUID organizerId, UUID sessionId, UUID zoneId,
                                             ManagementZoneRequest request) {
        findSession(organizerId, sessionId);
        Zone zone = zoneRepository.findByIdAndSessionId(zoneId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Zone not found: " + zoneId));
        applyRequestToZone(zone, request);
        Zone saved = zoneRepository.save(zone);
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

    // ── Layout ─────────────────────────────────────────────

    @Override
    @Transactional
    public EventLayoutResponse applyLayout(UUID organizerId, UUID eventId,
                                           ManagementEventLayoutRequest request) {
        Event event = findEvent(organizerId, eventId);

        String layoutJson;
        if (request.useVenueLayout()) {
            if (request.venueId() != null) {
                Venue v = venueRepository.findById(request.venueId())
                        .orElseThrow(() -> new EntityNotFoundException("Venue not found: " + request.venueId()));
                event.setVenue(v);
            }
            Venue venue = event.getVenue();
            if (venue != null && venue.getLayout() != null) {
                layoutJson = venue.getLayout();
                // Store venue marker JSON to persist zone links for frontend restore
                try {
                    StringBuilder marker = new StringBuilder("{\"venueMode\":true,\"venueId\":\"")
                            .append(venue.getId()).append("\",\"zoneLinks\":{");
                    if (request.venueZoneLinks() != null) {
                        boolean first = true;
                        for (ManagementEventLayoutRequest.VenueZoneLink link : request.venueZoneLinks()) {
                            if (link.zoneId() == null) continue;
                            if (!first) marker.append(",");
                            marker.append("\"").append(link.venueZoneName()).append("\":\"")
                                    .append(link.zoneId()).append("\"");
                            first = false;
                        }
                    }
                    marker.append("}}");
                    event.setLayout(marker.toString());
                } catch (Exception e) {
                    event.setLayout(null);
                }
            } else if (request.customLayoutJson() != null && !request.customLayoutJson().isBlank()) {
                layoutJson = request.customLayoutJson();
                event.setLayout(layoutJson);
            } else if (event.getLayout() != null) {
                layoutJson = event.getLayout();
            } else {
                throw new IllegalArgumentException(
                        "No layout available. Please select a venue with a layout or provide a custom layout.");
            }
        } else {
            if (request.customLayoutJson() == null || request.customLayoutJson().isBlank())
                throw new IllegalArgumentException("customLayoutJson must be provided when useVenueLayout is false.");
            layoutJson = request.customLayoutJson();
            event.setLayout(layoutJson);
            event.setVenue(null);
        }
        eventRepository.save(event);

        List<EventSession> sessions = sessionRepository.findAllByEventIdOrderByStartDateAsc(eventId);
        for (EventSession session : sessions) {
            if (request.useVenueLayout()
                    && request.venueZoneLinks() != null
                    && !request.venueZoneLinks().isEmpty()) {
                generateSeatsFromZoneLinks(session, request.venueZoneLinks());
            } else {
                generateSeatsFromLayout(session, layoutJson);
            }
        }

        return new EventLayoutResponse(eventId, layoutJson);
    }

    @Override
    @Transactional(readOnly = true)
    public EventLayoutResponse getLayout(UUID organizerId, UUID eventId) {
        Event event = findEvent(organizerId, eventId);
        String layoutJson = event.getLayout();
        if (layoutJson == null && event.getVenue() != null) {
            layoutJson = event.getVenue().getLayout();
        }
        return new EventLayoutResponse(eventId, layoutJson);
    }

    // ── Seats ──────────────────────────────────────────────

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
                                            ManagementSeatGenerateRequest request) {
        Zone zone = resolveZone(organizerId, sessionId, zoneId);
        seatRepository.deleteAllByZoneId(zone.getId());

        List<Seat> seats = new ArrayList<>();
        for (ManagementSeatRowRequest row : request.rows()) {
            String prefix = row.prefix() != null ? row.prefix() : "A";
            for (int col = 1; col <= row.count(); col++) {
                String colStr   = String.valueOf(col);
                String seatCode = prefix + col;
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
                                        ManagementSeatPriceOverrideRequest request) {
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

    // ── Seat generation from explicit venue zone links ─────
    private void generateSeatsFromZoneLinks(EventSession session,
                                            List<ManagementEventLayoutRequest.VenueZoneLink> links) {
        for (ManagementEventLayoutRequest.VenueZoneLink link : links) {
            if (link.zoneId() == null) continue;
            Zone zone = zoneRepository.findByIdAndSessionId(link.zoneId(), session.getId())
                    .orElse(null);
            if (zone == null) {
                log.warn("Zone {} not found in session {}", link.zoneId(), session.getId());
                continue;
            }
            if (Boolean.TRUE.equals(zone.getIsStanding())) {
                log.info("Zone '{}' is standing — no seats generated", zone.getName());
                continue;
            }
            if (zone.getCapacity() == null || zone.getCapacity() <= 0) {
                log.warn("Zone '{}' has no capacity — skipping seat generation", zone.getName());
                continue;
            }
            seatRepository.deleteAllByZoneId(zone.getId());
            int cols = (int) Math.round(Math.sqrt(zone.getCapacity()));
            int rows = (int) Math.ceil((double) zone.getCapacity() / cols);
            generateSeatsFromGrid(zone, rows, cols);
            log.info("Generated {} seats for zone '{}' via venue zone link", rows * cols, zone.getName());
        }
    }

    // ── Seat generation from grid dimensions ──────────────
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
                seat.setColName(String.valueOf(c).length() > 5
                        ? String.valueOf(c).substring(0, 5) : String.valueOf(c));
                seat.setSeatCode(seatCode);
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        log.info("Generated {}x{} = {} seats for zone '{}'",
                gridRows, gridCols, seats.size(), zone.getName());
    }

    // ── Seat generation from layout JSON ───────────────────
    private void generateSeatsFromLayout(EventSession session, String layoutJson) {
        try {
            JsonNode root = objectMapper.readTree(layoutJson);

            // Skip venue marker JSON — no seats to generate from it
            if (root.has("venueMode") && root.path("venueMode").asBoolean()) return;

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
                String zoneIdStr = zoneNode.path("zone_id").asText(null);
                String zoneName  = zoneNode.path("zone_name").asText();

                Zone matchedZone = null;
                if (zoneIdStr != null && !zoneIdStr.isBlank() && !"null".equals(zoneIdStr)) {
                    try {
                        UUID zoneId = UUID.fromString(zoneIdStr);
                        matchedZone = sessionZones.stream()
                                .filter(z -> z.getId().equals(zoneId))
                                .findFirst().orElse(null);
                    } catch (IllegalArgumentException ignored) {}
                }
                if (matchedZone == null) {
                    matchedZone = sessionZones.stream()
                            .filter(z -> z.getName().equalsIgnoreCase(zoneName))
                            .findFirst().orElse(null);
                }
                if (matchedZone == null) {
                    log.warn("Layout zone '{}' has no matching Zone in session {}", zoneName, session.getId());
                    continue;
                }
                if (Boolean.TRUE.equals(matchedZone.getIsStanding())) continue;

                int existingCount = seatRepository.countByZoneId(matchedZone.getId());
                if (existingCount > 0) {
                    log.info("Zone '{}' already has {} seats — skipping layout generation",
                            matchedZone.getName(), existingCount);
                    continue;
                }

                JsonNode seats = zoneNode.path("seats");
                if (!seats.isArray() || seats.isEmpty()) {
                    if (matchedZone.getCapacity() != null && matchedZone.getCapacity() > 0) {
                        int cols = (int) Math.round(Math.sqrt(matchedZone.getCapacity()));
                        int rows = (int) Math.ceil((double) matchedZone.getCapacity() / cols);
                        generateSeatsFromGrid(matchedZone, rows, cols);
                    }
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
                log.info("Generated {} seats for zone '{}' in session {}",
                        newSeats.size(), zoneName, session.getId());
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

    private void applyRequestToZone(Zone zone, ManagementZoneRequest req) {
        zone.setName(req.name());
        zone.setIsStanding(req.isStanding());
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