package com.ticket4u.dto.auth.internal;

import com.ticket4u.dto.user.UserResponse;

public record RefreshCreationResult(
        UserResponse userResponse,
        String refreshToken
) {
}
