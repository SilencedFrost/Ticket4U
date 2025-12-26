package com.ticket4u.dto.auth;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String message
) {
}
