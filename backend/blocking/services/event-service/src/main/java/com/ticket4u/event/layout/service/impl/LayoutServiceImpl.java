package com.ticket4u.event.layout.service.impl;

import com.ticket4u.core.dto.LayoutResponse;
import com.ticket4u.core.dto.ZoneResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.entity.EventSession;
import com.ticket4u.core.entity.Zone;
import com.ticket4u.event.layout.exceptions.EventSessionNotFoundException;
import com.ticket4u.event.layout.mapper.ZoneMapper;
import com.ticket4u.event.layout.repository.SessionRepository;
import com.ticket4u.event.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LayoutServiceImpl implements LayoutService {

    private final ZoneMapper zoneMapper;
    private final ObjectMapper objectMapper;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional(readOnly = true)
    public LayoutResponse getLayout(UUID sessionId) {

        EventSession session = sessionRepository.findById(sessionId).orElseThrow(() -> new EventSessionNotFoundException(sessionId));

        String layoutJson = resolveLayout(session.getEvent());
        List<ZoneResponse> zones = session.getZones().stream().map(zoneMapper::toDTO).toList();
        return new LayoutResponse(layoutJson, zones);
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
}