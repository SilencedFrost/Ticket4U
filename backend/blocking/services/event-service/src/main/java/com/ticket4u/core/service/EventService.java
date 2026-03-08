package com.ticket4u.core.service;

import com.ticket4u.core.dto.EventResponse;

import java.util.UUID;

public interface EventService {
    EventResponse findEvent(UUID id);
}
