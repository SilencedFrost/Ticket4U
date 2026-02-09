package com.ticket4u.feature.eventDetail.service;

import com.ticket4u.feature.eventDetail.dto.EventDetailResponse;

import java.util.UUID;

public interface EventDetailService {
    EventDetailResponse  getEventDetail(UUID id);
}
