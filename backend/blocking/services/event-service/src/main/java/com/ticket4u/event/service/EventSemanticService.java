package com.ticket4u.event.service;

import com.ticket4u.core.dto.EventSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface EventSemanticService {
    void storeEventVector(UUID id);
    List<EventSummaryResponse> findSimilarById(UUID id, int limit);
}
