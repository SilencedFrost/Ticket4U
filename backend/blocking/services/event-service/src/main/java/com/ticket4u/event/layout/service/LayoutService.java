package com.ticket4u.event.layout.service;

import com.ticket4u.core.dto.LayoutResponse;

import java.util.UUID;

public interface LayoutService {
    LayoutResponse getLayout(UUID sessionId);
}