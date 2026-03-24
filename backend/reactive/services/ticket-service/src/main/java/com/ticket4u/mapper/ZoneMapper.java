package com.ticket4u.mapper;

import com.ticket4u.dto.SeatResponse;
import com.ticket4u.dto.ZoneResponse;
import com.ticket4u.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ZoneMapper {

    private final SeatMapper seatMapper;

    public ZoneResponse toDTO(UUID zoneId, List<Ticket> zoneTickets) {
        String zoneName = zoneTickets.getFirst().getZoneName();

        List<SeatResponse> seats = zoneTickets.stream()
                .map(seatMapper::toDTO)
                .toList();

        return new ZoneResponse(zoneId, zoneName, seats, (long) zoneTickets.size());
    }
}
