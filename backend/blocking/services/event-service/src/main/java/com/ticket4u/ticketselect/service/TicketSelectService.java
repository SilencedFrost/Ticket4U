package com.ticket4u.ticketselect.service;

import com.ticket4u.ticketselect.dto.SeatingPlanResponse;

import java.util.UUID;

public interface TicketSelectService {
    SeatingPlanResponse getTicketSelectData(UUID eventId);
}