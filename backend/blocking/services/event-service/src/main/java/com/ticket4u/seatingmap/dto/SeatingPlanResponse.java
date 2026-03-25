package com.ticket4u.seatingmap.dto;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.dto.ZoneResponse;

import java.util.List;

public record SeatingPlanResponse(
        String layout,
        List<ZoneResponse> zones,
        List<SeatResponse> seats
) {}