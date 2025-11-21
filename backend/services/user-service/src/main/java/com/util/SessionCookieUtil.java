package com.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionCookieUtil {
    private static final String SESSION_COOKIE = "SESSION_KEY";
    private static final long SESSION_MAX_AGE = Duration.ofDays(7).toSeconds();

    public ResponseCookie createSessionCookie(String sessionKey) {
        return ResponseCookie.from(SESSION_COOKIE, sessionKey)
                .maxAge(SESSION_MAX_AGE)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .build();
    }

    public Optional<String> getSessionKey(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> SESSION_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public ResponseCookie createDeleteCookie() {
        return ResponseCookie.from(SESSION_COOKIE, "")
                .maxAge(0)
                .path("/")
                .build();
    }
}