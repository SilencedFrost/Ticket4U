package com.ticket4u.dto.auth.internal;

public record RefreshResult(
        String accessTokenCookie,
        String accessToken,
        String refreshTokenCookie
) {
}
