package com.ticket4u.crud.dto;

import java.util.UUID;

public record CrudProfileResponse(
        UUID id,
        String name,
        String avatarUrl
) {}
