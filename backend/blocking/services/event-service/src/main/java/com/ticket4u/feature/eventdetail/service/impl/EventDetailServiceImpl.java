package com.ticket4u.feature.eventdetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.eventdetail.client.UserClient;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.OrganizerDTO;
import com.ticket4u.feature.eventdetail.mapper.EventDetailMapper;
import com.ticket4u.feature.eventdetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventdetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventCardDTO;
import com.ticket4u.feature.homepage.mapper.HomePageMapper;
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
    private final HomePageService homePageService;
    private final UserClient userClient;
    private final EventDetailMapper eventDetailMapper;
    private final HomePageMapper homePageMapper;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        EventDetailResponse response = eventDetailMapper.toResponse(event);

        OrganizerDTO organizer = userClient.getOrganizerById(event.getOrganizerId());

        String minPrice = formatPrice(calculateMinPrice(event));
        String maxPrice = formatPrice(calculateMaxPrice(event));

        return new EventDetailResponse(
                response.eventId(),
                response.eventTitle(),
                response.date(),
                response.time(),
                response.address(),
                response.description(),
                minPrice,
                maxPrice,
                response.categoryId(),
                response.imgEvent(),
                organizer,
                response.showtimes()
        );
    }

    private double calculateMinPrice(Event event) {
        return event.getZones().stream()
                .mapToDouble(z -> z.getPrice().doubleValue())
                .min().orElse(0.0);
    }

    private double calculateMaxPrice(Event event) {
        return event.getZones().stream()
                .mapToDouble(z -> z.getPrice().doubleValue())
                .max().orElse(0.0);
    }

    private String formatPrice(double price) {
        return com.ticket4u.utils.PriceFormatter.format(price);
    }

    @Override
    public List<EventCardDTO> getRelatedEvents(UUID currentId, Integer categoryId, String address) {
        Set<EventCardDTO> results = new LinkedHashSet<>();
        String city = extractCity(address);

        // 1. Same Category (limit to 8 events)
        addEvents(results, homePageService.getFilteredEvents(null, null, List.of(categoryId), null, 0, 8), currentId);

        // 2. Same City (Fallback)
        if (results.size() < 8 && !city.isEmpty()) {
            String searchCity = city.toLowerCase();
            List<EventCardDTO> byCity = homePageService.getAllEventsWithMinPrice().stream()
                    .filter(e -> e.getAddressLine().toLowerCase().contains(searchCity))
                    .toList();
            addEvents(results, byCity, currentId);
        }

        // 3. Upcoming Events (Final Fallback)
        if (results.size() < 8) {
            addEvents(results, homePageService.getSpecialEvents(), currentId);
        }

        // 4. Get any remaining events (Suggest randomly if still under 8)
        if (results.size() < 8) {
            List<EventCardDTO> allOtherEvents = homePageService.getAllEventsWithMinPrice();
            addEvents(results, allOtherEvents, currentId);
        }

        return results.stream().limit(8).toList();
    }

    private String extractCity(String address) {
        if (address == null || !address.contains(",")) return "";
        String[] parts = address.split(",");
        return parts[parts.length - 1].trim();
    }

    private void addEvents(Set<EventCardDTO> target, List<EventCardDTO> source, UUID excludeId) {
        if (source == null) return;
        for (EventCardDTO event : source) {
            if (target.size() >= 8) break;

            boolean isSameEvent = event.getId().equals(excludeId);
            boolean isDuplicate = target.stream().anyMatch(e -> e.getId().equals(event.getId()));

            if (!isSameEvent && !isDuplicate) {
                target.add(event);
            }
        }
    }
}
