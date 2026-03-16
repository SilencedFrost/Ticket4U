package com.ticket4u.crud.dto;

import java.util.UUID;

public record EventLayoutResponse(
        UUID eventId,
        String eventLayout
) {}
