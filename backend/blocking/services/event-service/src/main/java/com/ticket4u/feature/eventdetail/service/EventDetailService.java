package com.ticket4u.feature.eventdetail.service;

import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.homepage.dto.EventCardResponse;

import java.util.List;
import java.util.UUID;

public interface EventDetailService {
    EventDetailResponse  getEventDetail(UUID id);
    List<EventCardResponse> getRelatedEvents(UUID currentId, Integer categoryId, String address);
}
