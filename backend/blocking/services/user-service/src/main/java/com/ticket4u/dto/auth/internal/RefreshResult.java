package com.ticket4u.dto.auth.internal;

import com.ticket4u.dto.auth.AuthResponse;

public record RefreshResult(
        String accessTokenCookie,
        String accessToken,
        String refreshTokenCookie,
        AuthResponse authResponse
) {
}
