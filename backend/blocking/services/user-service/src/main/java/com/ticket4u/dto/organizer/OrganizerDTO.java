package com.ticket4u.dto.organizer;

import java.util.UUID;

public record OrganizerDTO (
         UUID id,
         String name,
         String logo_url,
         String description
) {}
