package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.core.EventSession;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.mapper.ZoneMapper;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.core.projection.EventWithCategoryProjection;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.mapper.EventSummaryMapper;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventRepository eventRepository;
    private final EventDetailMapper eventDetailMapper;
    private final EventSummaryMapper eventSummaryMapper;
    private final ZoneMapper zoneMapper;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        if (event.getSessions() == null || event.getSessions().isEmpty()) {
            throw new EntityNotFoundException("No sessions found for event ID: " + id);
        }

        List<Zone> allZones = event.getSessions().stream()
                .flatMap(session -> session.getZones().stream())
                .toList();

        BigDecimal minPrice = calculateMinPrice(allZones);
        BigDecimal maxPrice = calculateMaxPrice(allZones);

        OffsetDateTime startDate = event.getSessions().stream()
                .findFirst()
                .map(EventSession::getStartDate)
                .orElse(null);

        return eventDetailMapper.toResponse(event, startDate, minPrice, maxPrice);
    }

    private BigDecimal calculateMinPrice(List<Zone> zones) {
        if (zones == null || zones.isEmpty())
            return BigDecimal.ZERO;
        return zones.stream()
                .map(Zone::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateMaxPrice(List<Zone> zones) {
        if (zones == null || zones.isEmpty())
            return BigDecimal.ZERO;
        return zones.stream()
                .map(Zone::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<EventSummaryResponse> getRelatedEvents(UUID currentId) {
        Event currentEvent = eventRepository.findById(currentId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        Integer categoryId = currentEvent.getCategory().getId();
        String city = extractCityForSearch(currentEvent.getAddressLine());

        List<EventWithCategoryProjection> results = eventRepository.findRelatedEvents(
                currentId,
                categoryId,
                city,
                8);

        return results.stream()
                .map(eventSummaryMapper::toEventSummaryResponse)
                .toList();
    }

    private String extractCityForSearch(String address) {
        if (address == null || address.isBlank()) {
            return "";
        }

        if (!address.contains(",")) {
            return address.trim();
        }

        String[] parts = address.split(",");
        String city = parts[parts.length - 1].trim();

        return city.replaceAll("\\s+", " ").trim();
    }
}
