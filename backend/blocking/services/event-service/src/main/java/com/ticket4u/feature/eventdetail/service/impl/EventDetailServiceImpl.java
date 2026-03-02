package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.eventdetail.client.UserClient;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.OrganizerResponse;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.mapper.ZoneMapper;
import com.ticket4u.feature.eventdetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventCardResponse;
import com.ticket4u.feature.homepage.service.HomePageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventDetailRepository eventRepository;
    private final EventDetailMapper eventDetailMapper;
    private final HomePageService homePageService;
    private final ZoneMapper zoneMapper;
    private final UserClient userClient;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        EventDetailResponse response = eventDetailMapper.toResponse(event, zoneMapper);

        OrganizerResponse organizer = userClient.getOrganizerById(event.getOrganizerId());

        double minPrice = event.getZones().stream()
                .mapToDouble(z -> z.getPrice().doubleValue())
                .min().orElse(0.0);

        double maxPrice = event.getZones().stream()
                .mapToDouble(z -> z.getPrice().doubleValue())
                .max().orElse(0.0);

        return new EventDetailResponse(
                response.eventId(),
                response.eventTitle(),
                response.startDate(),
                response.address(),
                response.description(),
                String.valueOf(minPrice),
                String.valueOf(maxPrice),
                response.categoryId(),
                response.imgEvent(),
                organizer,
                response.showtimes()
        );
    }

    @Override
    public List<EventCardResponse> getRelatedEvents(UUID currentId, Integer categoryId, String address) {
        Set<EventCardResponse> results = new LinkedHashSet<>();
        String city = extractCity(address);

        // 1. Same Category (limit to 8 events)
        addEvents(results, homePageService.getFilteredEvents(null, null, List.of(categoryId), null, 0, 8), currentId);

        // 2. Same City (Fallback)
        if (results.size() < 8 && !city.isEmpty()) {
            String searchCity = city.toLowerCase();
            List<EventCardResponse> byCity = homePageService.getAllEventsWithMinPrice().stream()
                    .filter(e -> e.addressLine().toLowerCase().contains(searchCity))
                    .toList();
            addEvents(results, byCity, currentId);
        }

        // 3. Upcoming Events (Final Fallback)
        if (results.size() < 8) {
            addEvents(results, homePageService.getSpecialEvents(), currentId);
        }

        // 4. Get any remaining events (Suggest randomly if still under 8)
        if (results.size() < 8) {
            List<EventCardResponse> allOtherEvents = homePageService.getAllEventsWithMinPrice();
            addEvents(results, allOtherEvents, currentId);
        }

        return results.stream().limit(8).toList();
    }

    private String extractCity(String address) {
        if (address == null || !address.contains(",")) return "";
        String[] parts = address.split(",");
        return parts[parts.length - 1].trim();
    }

    private void addEvents(Set<EventCardResponse> target, List<EventCardResponse> source, UUID excludeId) {
        if (source == null) return;
        for (EventCardResponse event : source) {
            if (target.size() >= 8) break;

            boolean isSameEvent = event.id().equals(excludeId);
            boolean isDuplicate = target.stream().anyMatch(e -> e.id().equals(event.id()));

            if (!isSameEvent && !isDuplicate) {
                target.add(event);
            }
        }
    }
}
