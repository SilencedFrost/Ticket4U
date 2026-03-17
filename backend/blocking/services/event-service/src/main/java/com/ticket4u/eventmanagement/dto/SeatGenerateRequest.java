package com.ticket4u.eventmanagement.dto;

import java.util.List;

public record SeatGenerateRequest(
        List<SeatRowRequest> rows
) {}