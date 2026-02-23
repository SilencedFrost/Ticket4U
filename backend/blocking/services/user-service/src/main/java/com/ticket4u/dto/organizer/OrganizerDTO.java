package com.ticket4u.dto.organizer;

import java.util.UUID;

public record OrganizerDTO (
         UUID id,
         String name,
         String avatar,
         String description
) {}
