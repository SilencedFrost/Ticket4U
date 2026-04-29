package com.ticket4u.core.dto;

import java.util.List;

public record CategoryWithEventResponse(
        Integer id,
        String name,
        List<EventSummaryResponse> events
) {
}
