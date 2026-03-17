package com.ticket4u.organizer.dto;

import java.util.List;

public record SeatGenerateRequest(
        List<SeatRowRequest> rows
) {}