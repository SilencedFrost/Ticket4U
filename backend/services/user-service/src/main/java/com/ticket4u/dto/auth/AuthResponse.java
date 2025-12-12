package com.ticket4u.dto.auth;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        Integer roleId,
        String username,
        String email
) {
}
