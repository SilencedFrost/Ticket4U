package com.ticket4u.ticketselect.service;

import com.ticket4u.ticketselect.dto.TicketSelectResponse;

import java.util.UUID;

public interface TicketSelectService {
    TicketSelectResponse getTicketSelectData(UUID eventId);
}