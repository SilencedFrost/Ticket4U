package com.ticket4u.feature.ticketselect.service.impl;

import com.ticket4u.core.Event;
import com.ticket4u.feature.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.feature.ticketselect.mapper.TicketSelectMapper;
import com.ticket4u.feature.ticketselect.repository.TicketSelectRepository;
import com.ticket4u.feature.ticketselect.service.TicketSelectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketSelectServiceImpl implements TicketSelectService {

    private final TicketSelectRepository ticketSelectRepository;
    private final TicketSelectMapper ticketSelectMapper;

    @Override
    @Transactional(readOnly = true)
    public TicketSelectResponse getTicketSelectData(UUID eventId) {
        // First query: fetch event + zones (for ticket info panel)
        Event event = ticketSelectRepository.findWithZonesById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        // Second query: fetch event layouts + venue layout JSON + modifications
        // We merge the layouts into the same event object
        ticketSelectRepository.findWithLayoutsById(eventId)
                .ifPresent(eventWithLayouts ->
                        event.setEventLayouts(eventWithLayouts.getEventLayouts())
                );

        return ticketSelectMapper.toResponse(event);
    }
}