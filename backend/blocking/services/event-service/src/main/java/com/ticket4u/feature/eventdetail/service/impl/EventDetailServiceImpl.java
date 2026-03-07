package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.entity.Event;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.mapper.ZoneMapper;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.core.dto.EventWithCategoryDto;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.mapper.EventSummaryMapper;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        return eventDetailMapper.toResponse(event);
    }

    @Override
    public List<EventSummaryResponse> getRelatedEvents(UUID currentId) {
        Event currentEvent = eventRepository.findById(currentId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        Integer categoryId = currentEvent.getCategory().getId();
        String city = extractCityForSearch(currentEvent.getAddressLine());

        List<EventWithCategoryDto> results = eventRepository.findRelatedEvents(
                currentId,
                categoryId,
                city,
                8);

        return results.stream()
                .map(eventSummaryMapper::toDTO)
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
