package com.ticket4u.mapper;

import com.ticket4u.dto.AvailabilityResponse;
import com.ticket4u.dto.ZoneResponse;
import com.ticket4u.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final ZoneMapper zoneMapper;

    public AvailabilityResponse toAvailabilityResponse(UUID eventId, List<Ticket> tickets) {
        List<ZoneResponse> zones = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getZoneId))
                .entrySet().stream()
                .map(entry -> zoneMapper.toDTO(entry.getKey(), entry.getValue()))
                .toList();

        return new AvailabilityResponse(eventId, zones);
    }
}
