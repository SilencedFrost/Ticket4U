package com.ticket4u.feature.eventDetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.eventDetail.client.UserClient;
import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventDetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventDetail.service.EventDetailService;
import com.ticket4u.feature.homepage.dto.EventCardDTO;
import com.ticket4u.feature.homepage.service.HomePageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventDetailRepository eventRepository;
    private final HomePageService homePageService;
    private final UserClient userClient;

    @Override
    public EventDetailResponse getEventDetail(UUID id) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        EventDetailResponse.OrganizerDTO organizer = userClient.getOrganizerById(event.getOrganizerId());

        EventDetailResponse response = new EventDetailResponse();
        response.setEventId(event.getId());
        response.setEventTitle(event.getName());
        response.setDescription(event.getDescription());

        response.setAddress(event.getAddressLine());
        response.setCategoryId(event.getCategory().getId());
        String timeRange = event.getStartDate().format(timeFormatter) + " - " +
                (event.getEndDate() != null ? event.getEndDate().format(timeFormatter) : "N/A");
        response.setDate(event.getStartDate().format(dateFormatter));
        response.setTime(timeRange);

        // Map Images
        EventDetailResponse.ImageEventDTO images = new EventDetailResponse.ImageEventDTO();
        images.setHeroUrl(event.getBannerUrl());
        images.setSeatMapUrl(event.getContent().getSeatingPlanImageUrl());
        response.setImgEvent(images);

        // Showtimes
        EventDetailResponse.ShowtimeDTO showtime = new EventDetailResponse.ShowtimeDTO();
        showtime.setId(event.getId().toString());
        showtime.setDate(event.getStartDate().format(dateFormatter));
        showtime.setTime(timeRange);

        double min = event.getZones().stream()
                .mapToDouble(zone -> zone.getPrice().doubleValue())
                .min().orElse(0.0);

        double max = event.getZones().stream()
                .mapToDouble(zone -> zone.getPrice().doubleValue())
                .max().orElse(0.0);

        response.setMinPrice(String.format("%,.0fđ", min));
        response.setMaxPrice(String.format("%,.0fđ", max));

        // SeatTypes
        if (event.getZones() != null) {
            List<EventDetailResponse.SeatTypeDTO> seatTypes = event.getZones().stream().map(zone -> {
                EventDetailResponse.SeatTypeDTO stDto = new EventDetailResponse.SeatTypeDTO();
                stDto.setId(zone.getId());
                stDto.setName(zone.getName());
                stDto.setPrice(String.format("%,.0f đ", zone.getPrice().doubleValue()));

                // Calculate Tickets: Available = Capacity - QuantitySold
                int available = (zone.getCapacity() != null ? zone.getCapacity() : 0)
                        - (zone.getQuantitySold() != null ? zone.getQuantitySold() : 0);
                stDto.setAvailable(Math.max(0, available));

                if (zone.getContent() != null) {
                    stDto.setDescription(zone.getContent().getDescription());
                    stDto.setImage(zone.getContent().getGiftImageUrl());
                    stDto.setBenefits(zone.getContent().getPerksAsList());
                }

                return stDto;
            }).collect(Collectors.toList());

            showtime.setSeatTypes(seatTypes);
        }

        response.setShowtimes(List.of(showtime));
        response.setOrganizer(organizer);

        return response;
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

            boolean isDuplicate = target.stream().anyMatch(e -> e.getId().equals(event.getId()));

            if (!event.getId().equals(excludeId) && !isDuplicate) {
                target.add(event);
            }
        }
    }
}
