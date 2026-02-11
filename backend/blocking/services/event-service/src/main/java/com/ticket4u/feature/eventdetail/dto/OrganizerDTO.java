package com.ticket4u.feature.eventdetail.dto;

import java.util.UUID;

public record OrganizerDTO (
        UUID id,
         String name,
         String avatar,
         String description
) {}
