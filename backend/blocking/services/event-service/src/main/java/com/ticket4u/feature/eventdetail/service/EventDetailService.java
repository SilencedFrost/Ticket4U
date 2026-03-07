package com.ticket4u.feature.eventdetail.service;

import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;

import java.util.List;
import java.util.UUID;

public interface EventDetailService {
    EventDetailResponse  getEventDetail(UUID id);
    List<EventSummaryResponse> getRelatedEvents(UUID currentId);
}
