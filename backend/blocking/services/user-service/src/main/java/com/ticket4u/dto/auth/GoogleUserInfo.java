package com.ticket4u.dto.auth;

public record GoogleUserInfo(
        String email,
        String name,
        String picture
) {
}
