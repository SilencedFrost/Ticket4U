package com.ticket4u.organizer.dto;

import java.util.UUID;

public record OrganizerProfileResponse(
        UUID id,
        String name,
        String avatarUrl
) {}
