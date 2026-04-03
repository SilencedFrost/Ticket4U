package com.ticket4u.event.service;

import com.ticket4u.core.dto.EventSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Higher, more business-heavy service than EventService
 */
public interface EventDomainService {
    List<EventSummaryResponse> findRelatedEvents(UUID id);
    List<EventSummaryResponse> findUpcomingPurchasableEventsLimit(Integer limit);
    List<EventSummaryResponse> findRandomEvent(Integer limit, Integer samplingMultiplier);
    List<EventSummaryResponse> searchEvents(String query, Pageable pageable);
}
