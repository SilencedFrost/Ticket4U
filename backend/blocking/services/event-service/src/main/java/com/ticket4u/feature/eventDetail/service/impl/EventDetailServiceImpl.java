package com.ticket4u.feature.eventDetail.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.eventDetail.client.UserClient;
import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventDetail.repository.EventDetailRepository;
import com.ticket4u.feature.eventDetail.service.EventDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {
    private final EventDetailRepository eventRepository;
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
}
