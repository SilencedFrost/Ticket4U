package com.ticket4u.core.dto;

import java.util.UUID;

public record EventLayoutResponse(
        UUID eventId,
        String eventLayout
) {}
