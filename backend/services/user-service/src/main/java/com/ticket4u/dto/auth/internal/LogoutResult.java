package com.ticket4u.dto.auth.internal;

import com.ticket4u.dto.auth.AuthResponse;

public record LogoutResult(
        String accessTokenCookie,
        String refreshTokenCookie
) {
}
