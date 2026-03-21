package com.ticket4u.ticketselect.service.impl;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.dto.ZoneResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.entity.EventSession;
import com.ticket4u.core.entity.Seat;
import com.ticket4u.core.entity.Zone;
import com.ticket4u.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.ticketselect.repository.TicketSelectRepository;
import com.ticket4u.ticketselect.repository.TicketSelectSeatRepository;
import com.ticket4u.ticketselect.service.TicketSelectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketSelectServiceImpl implements TicketSelectService {

    private final TicketSelectRepository     ticketSelectRepository;
    private final TicketSelectSeatRepository seatRepository;
    private final ObjectMapper               objectMapper;

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Override
    @Transactional(readOnly = true)
    public TicketSelectResponse getTicketSelectData(UUID eventId) {
        Event event = ticketSelectRepository.findWithSessionsZonesAndVenueById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        String layoutJson = resolveLayout(event);

        EventSession firstSession = event.getSessions().stream()
                .filter(s -> s.getStartDate() != null)
                .min((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
                .orElse(null);

        List<ZoneResponse> zones = event.getSessions().stream()
                .filter(s -> s.getZones() != null)
                .flatMap(s -> s.getZones().stream())
                .map(this::mapZone)
                .toList();

        List<SeatResponse> seats = firstSession != null
                ? seatRepository.findAllBySessionId(firstSession.getId())
                .stream().map(this::mapSeat).toList()
                : Collections.emptyList();

        return new TicketSelectResponse(
                event.getId(),
                event.getName(),
                event.getAddressLine(),
                event.getBannerUrl(),
                firstSession != null ? format(firstSession.getStartDate()) : null,
                firstSession != null ? format(firstSession.getEndDate())   : null,
                event.getAboutVi(),
                event.getAboutEn(),
                layoutJson,
                zones,
                seats
        );
    }

    // ── Layout resolution ──────────────────────────────────
    // 1. event.layout = {"venueMode":true,"zoneLinks":{...}} → inject zone_ids into venue layout
    // 2. event.layout = custom floors JSON                   → use as-is
    // 3. event.layout = null && venue != null                → use venue.layout as-is
    // 4. both null                                           → no layout
    private String resolveLayout(Event event) {
        String stored = event.getLayout();

        if (stored != null) {
            try {
                JsonNode node = objectMapper.readTree(stored);
                if (node.has("venueMode") && node.path("venueMode").asBoolean()) {
                    // Extract zoneLinks: { "VIP": "zone-uuid", "GA": "zone-uuid" }
                    Map<String, String> zoneLinks = new HashMap<>();
                    JsonNode linksNode = node.path("zoneLinks");
                    if (linksNode.isObject()) {
                        linksNode.properties().forEach(entry ->
                                zoneLinks.put(entry.getKey(), entry.getValue().asText())
                        );
                    }

                    if (event.getVenue() != null && event.getVenue().getLayout() != null) {
                        return injectZoneIds(event.getVenue().getLayout(), zoneLinks);
                    }
                    log.warn("Event {} has venue marker but no venue layout", event.getId());
                    return null;
                }
            } catch (Exception e) {
                log.warn("Event {} layout parse failed: {}", event.getId(), e.getMessage());
            }
            // Custom layout — return as-is
            return stored;
        }

        // No stored layout — fall back to venue default (no zone links)
        if (event.getVenue() != null && event.getVenue().getLayout() != null) {
            return event.getVenue().getLayout();
        }

        return null;
    }

    // ── Inject zone_ids into venue layout JSON ─────────────
    // Venue layout has zones with zone_name but no zone_id.
    // We inject zone_id from the zoneLinks map so the frontend
    // can match zones to actual Zone entities and load seats.
    private String injectZoneIds(String venueLayout, Map<String, String> zoneLinks) {
        if (zoneLinks.isEmpty()) return venueLayout;
        try {
            JsonNode root = objectMapper.readTree(venueLayout);

            // Handle both { floors: [...] } and flat { stage, zones } structures
            if (root.has("floors") && root.path("floors").isArray()) {
                ArrayNode floors = (ArrayNode) root.path("floors");
                for (JsonNode floor : floors) {
                    injectZoneIdsIntoFloor(floor, zoneLinks);
                }
            } else if (root.has("zones") && root.path("zones").isArray()) {
                injectZoneIdsIntoFloor(root, zoneLinks);
            }

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            log.warn("Failed to inject zone_ids into venue layout: {}", e.getMessage());
            return venueLayout;
        }
    }

    private void injectZoneIdsIntoFloor(JsonNode floorNode, Map<String, String> zoneLinks) {
        JsonNode zonesNode = floorNode.path("zones");
        if (!zonesNode.isArray()) return;
        for (JsonNode zone : zonesNode) {
            String zoneName = zone.path("zone_name").asText(null);
            if (zoneName != null && zoneLinks.containsKey(zoneName)) {
                ((ObjectNode) zone).put("zone_id", zoneLinks.get(zoneName));
            }
        }
    }

    // ── Mapping ────────────────────────────────────────────
    private ZoneResponse mapZone(Zone zone) {
        int available = Math.max(0,
                (zone.getCapacity()     != null ? zone.getCapacity()     : 0) -
                        (zone.getQuantitySold() != null ? zone.getQuantitySold() : 0));

        return new ZoneResponse(
                zone.getId(),
                zone.getName(),
                zone.getPrice(),
                available,
                zone.getDescriptionVi(),
                zone.getDescriptionEn(),
                zone.getGiftImageUrl(),
                zone.getPerks()
        );
    }

    private SeatResponse mapSeat(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getZone().getId(),
                seat.getName(),
                seat.getRowName(),
                seat.getColName(),
                seat.getSeatCode(),
                seat.getStatus() != null ? seat.getStatus().name() : "AVAILABLE",
                seat.getPriceOverride()
        );
    }

    private String format(OffsetDateTime dt) {
        return dt == null ? null : dt.format(DT_FMT);
    }
}