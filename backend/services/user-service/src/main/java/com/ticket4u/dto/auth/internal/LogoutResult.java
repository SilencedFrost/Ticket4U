package com.ticket4u.dto.auth.internal;

public record LogoutResult (
    String accessTokenCookie,
    String refreshTokenCookie
) {
}
