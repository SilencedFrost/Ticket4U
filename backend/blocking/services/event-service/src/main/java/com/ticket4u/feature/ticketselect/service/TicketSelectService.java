package com.ticket4u.feature.ticketselect.service;

import com.ticket4u.feature.ticketselect.dto.TicketSelectResponse;

import java.util.UUID;

public interface TicketSelectService {
    TicketSelectResponse getTicketSelectData(UUID eventId);
}