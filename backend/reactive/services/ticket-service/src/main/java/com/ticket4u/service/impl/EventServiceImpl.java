package com.ticket4u.service.impl;

import com.ticket4u.dto.AvailabilityResponse;
import com.ticket4u.mapper.EventMapper;
import com.ticket4u.repository.TicketRepository;
import com.ticket4u.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;

    @Override
    public AvailabilityResponse getAvailabilityByServiceId(UUID id) {
        return eventMapper.toAvailabilityResponse(id, ticketRepository.findByEventId(id));
    }
}
