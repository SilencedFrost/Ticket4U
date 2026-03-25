package com.ticket4u.eventlayout.service;

import com.ticket4u.eventlayout.dto.EventLayoutResponse;

import java.util.UUID;

public interface EventLayoutService {
    EventLayoutResponse getLayout(UUID eventId);
}