package com.ticket4u.service.impl;

import com.ticket4u.dto.TicketResponse;
import com.ticket4u.exception.TicketNotFoundException;
import com.ticket4u.mapper.TicketMapper;
import com.ticket4u.repository.TicketRepository;
import com.ticket4u.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketResponse findTicketOfUserById(UUID userId, UUID ticketId) {
        return ticketMapper.toDTO(ticketRepository.findByIdAndOrderUserId(ticketId, userId).orElseThrow(() -> new TicketNotFoundException(ticketId)));
    }
}
