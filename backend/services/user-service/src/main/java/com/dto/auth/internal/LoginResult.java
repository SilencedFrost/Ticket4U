package com.dto.auth.internal;

import com.dto.auth.AuthResponse;

public record LoginResult(
        AuthResponse authResponse,
        String accessTokenCookie,
        String refreshTokenCookie
) {
}
