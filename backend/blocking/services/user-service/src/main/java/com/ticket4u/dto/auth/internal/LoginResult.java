package com.ticket4u.dto.auth.internal;

import com.ticket4u.dto.auth.AuthResponse;

public record LoginResult(
        AuthResponse authResponse,
        String accessTokenCookie,
        String refreshTokenCookie
) {
}
