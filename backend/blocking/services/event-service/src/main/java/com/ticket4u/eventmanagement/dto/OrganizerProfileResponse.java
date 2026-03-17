package com.ticket4u.eventmanagement.dto;

import java.util.UUID;

public record OrganizerProfileResponse(
        UUID id,
        String name,
        String avatarUrl
) {}
