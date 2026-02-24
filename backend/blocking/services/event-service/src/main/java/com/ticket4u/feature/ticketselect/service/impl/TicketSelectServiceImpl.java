package com.ticket4u.feature.ticketselect.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.feature.ticketselect.mapper.TicketSelectMapper;
import com.ticket4u.feature.ticketselect.repository.TicketSelectRepository;
import com.ticket4u.feature.ticketselect.service.TicketSelectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketSelectServiceImpl implements TicketSelectService {

    private final TicketSelectRepository ticketSelectRepository;
    private final TicketSelectMapper ticketSelectMapper;

    @Override
    public TicketSelectResponse getTicketSelectData(UUID eventId) {
        Event event = ticketSelectRepository.findWithZonesById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        return ticketSelectMapper.toResponse(event);
    }
}