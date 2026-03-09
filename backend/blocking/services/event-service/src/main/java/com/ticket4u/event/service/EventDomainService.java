package com.ticket4u.event.service;

import com.ticket4u.core.dto.EventSummaryResponse;

import java.util.List;
import java.util.UUID;

/**
 * Higher, more business-heavy service than EventService
 */
public interface EventDomainService {
    List<EventSummaryResponse> findRelatedEvents(UUID id);
    List<EventSummaryResponse> findLatestEventsLimit(Integer limit);
}
