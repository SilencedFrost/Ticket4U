package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.mapper.ZoneMapper;
import com.ticket4u.feature.eventdetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import com.ticket4u.feature.homepage.mapper.NativeQueryMapper;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventDetailRepository eventRepository;
    private final EventDetailMapper eventDetailMapper;
    private final NativeQueryMapper nativeQueryMapper;
    private final ZoneMapper zoneMapper;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        BigDecimal minPrice = calculateMinPrice(event.getZones());
        BigDecimal maxPrice = calculateMaxPrice(event.getZones());

        List<ShowtimeResponse> showtimes = buildShowtimes(event);

        return eventDetailMapper.toResponse(event, minPrice, maxPrice, showtimes);
    }

    private List<ShowtimeResponse> buildShowtimes(Event event) {
        if (event.getZones() == null || event.getZones().isEmpty()) {
            return Collections.emptyList();
        }

        List<SeatTypeResponse> seatTypes = zoneMapper.toSeatTypeResponse(event.getZones());

        return List.of(new ShowtimeResponse(
                event.getId().toString(),
                event.getStartDate(),
                seatTypes));
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
                .map(nativeQueryMapper::toEventSummaryResponse)
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
