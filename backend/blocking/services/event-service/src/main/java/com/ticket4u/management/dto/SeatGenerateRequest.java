package com.ticket4u.management.dto;

import java.util.List;

public record SeatGenerateRequest(
        List<SeatRowRequest> rows
) {}