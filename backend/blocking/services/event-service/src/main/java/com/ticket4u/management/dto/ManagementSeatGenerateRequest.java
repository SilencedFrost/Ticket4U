package com.ticket4u.management.dto;

import java.util.List;

public record ManagementSeatGenerateRequest(
        List<ManagementSeatRowRequest> rows
) {}