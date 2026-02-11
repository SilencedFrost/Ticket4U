package com.ticket4u.feature.eventDetail.service;

import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;
import com.ticket4u.feature.homepage.dto.EventCardDTO;

import java.util.List;
import java.util.UUID;

public interface EventDetailService {
    EventDetailResponse  getEventDetail(UUID id);
    List<EventCardDTO> getRelatedEvents(UUID currentId, Integer categoryId, String address);
}
