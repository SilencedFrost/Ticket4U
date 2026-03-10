package com.ticket4u.core.dto;

import java.util.List;

public record CategoryResponse(
        Integer id,
        String name,
        List<EventSummaryResponse> events
) {
}
