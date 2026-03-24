package com.ticket4u.service;

import com.ticket4u.dto.AvailabilityResponse;

import java.util.UUID;

public interface EventService {
    AvailabilityResponse getAvailabilityByServiceId(UUID id);
}
