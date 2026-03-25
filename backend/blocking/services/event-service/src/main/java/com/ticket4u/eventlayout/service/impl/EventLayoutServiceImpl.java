package com.ticket4u.eventlayout.service.impl;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.dto.ZoneResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.entity.EventSession;
import com.ticket4u.core.entity.Seat;
import com.ticket4u.core.entity.Zone;
import com.ticket4u.eventlayout.dto.EventLayoutResponse;
import com.ticket4u.eventlayout.repository.EventLayoutRepository;
import com.ticket4u.eventlayout.repository.EventLayoutSeatRepository;
import com.ticket4u.eventlayout.service.EventLayoutService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventLayoutServiceImpl implements EventLayoutService {

    private final EventLayoutRepository     eventLayoutRepository;
    private final EventLayoutSeatRepository seatRepository;
    private final ObjectMapper              objectMapper;

    @Override
    @Transactional(readOnly = true)
    public EventLayoutResponse getLayout(UUID eventId) {
        Event event = eventLayoutRepository.findWithSessionsZonesAndVenueById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        String layoutJson = resolveLayout(event);

        EventSession firstSession = event.getSessions().stream()
                .filter(s -> s.getStartDate() != null)
                .min(Comparator.comparing(EventSession::getStartDate))
                .orElse(null);

        Map<UUID, List<SeatResponse>> seatsByZone = new HashMap<>();
        if (firstSession != null) {
            seatRepository.findAllBySessionId(firstSession.getId())
                    .forEach(seat -> seatsByZone
                            .computeIfAbsent(seat.getZone().getId(), k -> new ArrayList<>())
                            .add(mapSeat(seat)));
        }

        List<ZoneResponse> zones = event.getSessions().stream()
                .filter(s -> s.getZones() != null)
                .flatMap(s -> s.getZones().stream())
                .map(zone -> mapZone(zone, seatsByZone.getOrDefault(zone.getId(), Collections.emptyList())))
                .toList();

        return new EventLayoutResponse(layoutJson, zones);
    }

    private String resolveLayout(Event event) {
        String stored = event.getLayout();
        if (stored != null) {
            try {
                JsonNode node = objectMapper.readTree(stored);
                if (node.has("venueMode") && node.path("venueMode").asBoolean()) {
                    Map<String, String> zoneLinks = new HashMap<>();
                    JsonNode linksNode = node.path("zoneLinks");
                    if (linksNode.isObject()) {
                        linksNode.properties().forEach(entry ->
                                zoneLinks.put(entry.getKey(), entry.getValue().asText()));
                    }
                    if (event.getVenue() != null && event.getVenue().getLayout() != null) {
                        List<Zone> sessionZones = event.getSessions().stream()
                                .filter(s -> s.getZones() != null)
                                .flatMap(s -> s.getZones().stream())
                                .toList();
                        return injectZoneIds(event.getVenue().getLayout(), zoneLinks, sessionZones);
                    }
                    log.warn("Event {} has venue marker but no venue layout", event.getId());
                    return null;
                }
            } catch (Exception e) {
                log.warn("Event {} layout parse failed: {}", event.getId(), e.getMessage());
            }
            return stored;
        }
        if (event.getVenue() != null && event.getVenue().getLayout() != null)
            return event.getVenue().getLayout();
        return null;
    }

    private String injectZoneIds(String venueLayout, Map<String, String> zoneLinks,
                                 List<Zone> sessionZones) {
        if (zoneLinks.isEmpty()) return venueLayout;
        try {
            JsonNode root = objectMapper.readTree(venueLayout);
            Map<String, Zone> zoneById = new HashMap<>();
            for (Zone z : sessionZones) zoneById.put(z.getId().toString(), z);
            if (root.has("floors") && root.path("floors").isArray()) {
                for (JsonNode floor : root.path("floors"))
                    injectZoneIdsIntoFloor(floor, zoneLinks, zoneById);
            } else if (root.has("zones") && root.path("zones").isArray()) {
                injectZoneIdsIntoFloor(root, zoneLinks, zoneById);
            }
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            log.warn("Failed to inject zone_ids into venue layout: {}", e.getMessage());
            return venueLayout;
        }
    }

    private void injectZoneIdsIntoFloor(JsonNode floorNode, Map<String, String> zoneLinks,
                                        Map<String, Zone> zoneById) {
        JsonNode zonesNode = floorNode.path("zones");
        if (!zonesNode.isArray()) return;
        for (JsonNode zone : zonesNode) {
            String zoneName = zone.path("zone_name").asText(null);
            if (zoneName == null || !zoneLinks.containsKey(zoneName)) continue;
            String zoneId = zoneLinks.get(zoneName);
            ((ObjectNode) zone).put("zone_id", zoneId);
            Zone actual = zoneById.get(zoneId);
            if (actual != null) {
                ((ObjectNode) zone).put("accessible", !Boolean.TRUE.equals(actual.getIsStanding()));
            }
        }
    }

    private ZoneResponse mapZone(Zone zone, List<SeatResponse> seats) {
        int available = Math.max(0,
                (zone.getCapacity()     != null ? zone.getCapacity()     : 0) -
                        (zone.getQuantitySold() != null ? zone.getQuantitySold() : 0));
        return new ZoneResponse(
                zone.getId(), zone.getName(), zone.getPrice(), available,
                Boolean.TRUE.equals(zone.getIsStanding()),
                zone.getDescriptionVi(), zone.getDescriptionEn(),
                zone.getGiftImageUrl(), zone.getPerks(), seats
        );
    }

    private SeatResponse mapSeat(Seat seat) {
        return new SeatResponse(
                seat.getId(), seat.getZone().getId(), seat.getName(),
                seat.getRowName(), seat.getColName(), seat.getSeatCode(),
                seat.getStatus() != null ? seat.getStatus().name() : "AVAILABLE",
                seat.getPriceOverride()
        );
    }
}