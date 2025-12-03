package com.ticket4u.util;

import com.ticket4u.constant.SameSite;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    public static ResponseCookie.ResponseCookieBuilder secureBuilder(String key, String value) {
        return ResponseCookie.from(key, value)
                .sameSite(SameSite.NONE.getScheme())
                .httpOnly(true)
                .secure(true)
                .path("/");
    }

    public Optional<String> getCookieBody(Cookie[] cookies, String key) {
        return this.getCookie(cookies, key).map(Cookie::getValue);
    }

    public Optional<Cookie> getCookie(Cookie[] cookies, String key) {
        if (cookies == null) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst();
    }

    public ResponseCookie createDeleteCookie(String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite(SameSite.NONE.getScheme())
                .build();
    }

    public ResponseCookie createDeleteCookie(Cookie oldCookie) {
        return ResponseCookie.from(oldCookie.getName(), "")
                .maxAge(0)
                .path(oldCookie.getPath() != null ? oldCookie.getPath() : "/")
                .httpOnly(true)
                .secure(true)
                .sameSite(SameSite.NONE.getScheme())
                .build();
    }

}