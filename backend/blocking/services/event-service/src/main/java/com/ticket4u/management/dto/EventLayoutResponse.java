package com.ticket4u.management.dto;

import java.util.UUID;

public record EventLayoutResponse(
        UUID eventId,
        String eventLayout
) {}
