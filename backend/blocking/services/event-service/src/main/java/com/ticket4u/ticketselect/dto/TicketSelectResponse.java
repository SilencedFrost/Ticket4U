package com.ticket4u.ticketselect.dto;

import com.ticket4u.core.dto.SeatResponse;
import com.ticket4u.core.dto.ZoneResponse;

import java.util.List;
import java.util.UUID;

public record TicketSelectResponse(
        UUID             eventId,
        String           name,
        String           addressLine,
        String           bannerUrl,
        String           startDate,
        String           endDate,
        String           aboutVi,
        String           aboutEn,
        String           layout,          // resolved layout JSON (custom or venue default)
        List<ZoneResponse> zones,
        List<SeatResponse> seats
) {}