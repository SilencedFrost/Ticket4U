package com.ticket4u.util;

import com.ticket4u.core.entity.CustomUserDetails;
import com.ticket4u.core.exceptions.UnauthorizedException;

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

    public static UUID extractUserIdOrGet(CustomUserDetails principal, UUID defaultValue) {
        if (principal == null || principal.getUserId() == null) return defaultValue;
        return principal.getUserId();
    }

    public static UUID extractUserIdSafe(CustomUserDetails principal) {
        return extractUserIdOrGet(principal, null);
    }
}
