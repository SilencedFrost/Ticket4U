package com.ticket4u.util;

import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.exception.UnauthorizedException;

import java.util.UUID;

public final class AuthPrincipalUtil {

    private AuthPrincipalUtil() {
    }

    public static UUID extractUserIdOrThrow(CustomUserDetails principal) {
        if (principal == null || principal.getUserId() == null) {
            throw new UnauthorizedException("auth.error.unauthorized");
        }
        return principal.getUserId();
    }
}
