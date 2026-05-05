package com.ticket4u.service;

import com.ticket4u.dto.TicketResponse;

import java.util.UUID;

public interface TicketService {
    TicketResponse findTicketOfUserById(UUID userId, UUID ticketId);
}
