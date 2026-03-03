package com.ticket4u.feature.homepage.dto;

import java.util.List;

/**
 * Response DTO for homepage event list
 */
public record EventListResponse(
        List<EventSummaryResponse> events,
        Integer totalElements,
        Integer totalPages,
        Integer currentPage,
        Integer pageSize
) {}
