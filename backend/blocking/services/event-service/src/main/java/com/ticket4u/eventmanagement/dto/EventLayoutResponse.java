package com.ticket4u.eventmanagement.dto;

import java.util.UUID;

public record EventLayoutResponse(
        UUID eventId,
        String eventLayout
) {}
