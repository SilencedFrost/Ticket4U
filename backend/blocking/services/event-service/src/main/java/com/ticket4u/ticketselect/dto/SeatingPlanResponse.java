package com.ticket4u.ticketselect.dto;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.dto.ZoneResponse;

import java.util.List;
import java.util.UUID;

public record SeatingPlanResponse(
        String layout,
        List<ZoneResponse> zones,
        List<SeatResponse> seats
) {}