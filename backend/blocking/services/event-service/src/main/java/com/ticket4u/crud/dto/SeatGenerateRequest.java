package com.ticket4u.crud.dto;

import java.util.List;

public record SeatGenerateRequest(
        List<SeatRowRequest> rows
) {}