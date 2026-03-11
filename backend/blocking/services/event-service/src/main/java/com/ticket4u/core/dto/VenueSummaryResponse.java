package com.ticket4u.core.dto;

import java.util.UUID;

public record VenueSummaryResponse(
        UUID id,
        String name,
        String imageUrl
) {
}
