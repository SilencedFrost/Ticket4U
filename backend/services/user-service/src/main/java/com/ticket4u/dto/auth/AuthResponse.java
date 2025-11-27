package com.ticket4u.dto.auth;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        Integer roleId,
        String username
) {
}
