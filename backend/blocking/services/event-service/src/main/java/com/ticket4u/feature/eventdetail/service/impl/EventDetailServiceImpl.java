package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventSummaryResponse;
import com.ticket4u.feature.homepage.mapper.NativeQueryMapper;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventDetailRepository eventRepository;
    private final EventDetailMapper eventDetailMapper;
    private final NativeQueryMapper nativeQueryMapper;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        String minPrice = calculateMinPrice(event.getZones());
        String maxPrice = calculateMaxPrice(event.getZones());

        return eventDetailMapper.toResponse(event, minPrice, maxPrice);
    }

    private String calculateMinPrice(List<Zone> zones) {
        return zones == null || zones.isEmpty() ? "0" :
                String.valueOf(zones.stream().mapToDouble(z -> z.getPrice().doubleValue()).min().orElse(0.0));
    }

    private String calculateMaxPrice(List<Zone> zones) {
        return zones == null || zones.isEmpty() ? "0" :
                String.valueOf(zones.stream().mapToDouble(z -> z.getPrice().doubleValue()).max().orElse(0.0));
    }

    @Override
    public List<EventSummaryResponse> getRelatedEvents(UUID currentId) {
        Event currentEvent = eventRepository.findById(currentId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        Integer categoryId = currentEvent.getCategory().getId();
        String city = extractCity(currentEvent.getAddressLine());

        List<EventWithCategoryProjection> results = eventRepository.findRelatedEvents(
                currentId,
                categoryId,
                city,
                8
        );

        return results.stream()
                .map(nativeQueryMapper::toEventSummaryResponse)
                .toList();
    }

    private String extractCity(String address) {
        if (address == null || !address.contains(",")) return "";
        String[] parts = address.split(",");
        return parts[parts.length - 1].trim();
    }
}
