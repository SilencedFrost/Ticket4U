package com.ticket4u.core.service;

import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventResponse findEvent(UUID id);
    List<EventSummaryResponse> findRelatedEvents(UUID id);
}
