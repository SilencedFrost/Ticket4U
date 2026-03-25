package com.ticket4u.seatingmap.service;

import com.ticket4u.seatingmap.dto.SeatingPlanResponse;

import java.util.UUID;

public interface SeatingMapServiceImpl {
    SeatingPlanResponse getTicketSelectData(UUID eventId);
}