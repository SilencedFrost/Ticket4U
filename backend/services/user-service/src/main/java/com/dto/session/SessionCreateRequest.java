package com.dto.session;

public record SessionCreateRequest(
        Long userId,
        String sessionToken,
        String userAgent
) {
}
