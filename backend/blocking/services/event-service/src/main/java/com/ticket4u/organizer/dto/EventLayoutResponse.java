package com.ticket4u.organizer.dto;

import java.util.UUID;

public record EventLayoutResponse(
        UUID eventId,
        String eventLayout
) {}
