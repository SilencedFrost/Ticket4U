package com.util;

import com.constant.SameSite;
import com.constant.TokenConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    public CookieBuilder builder() {
        return new CookieBuilder();
    }

    public static class CookieBuilder {
        private long cookieAge = -1;
        private boolean httpOnly = false;
        private boolean secure = false;
        private SameSite sameSite = SameSite.LAX;
        private String path = "/";

        private boolean built = false;

        private void ensureNotBuilt() {
            if (built) {
                throw new IllegalStateException("Builder has already been used");
            }
        }

        public CookieBuilder maxAge(long seconds) {
            ensureNotBuilt();
            if(seconds < -1) {
                throw new IllegalArgumentException("Cookie age cannot be less than -1");
            }
            this.cookieAge = seconds;
            return this;
        }

        public CookieBuilder maxAge(Duration duration) {
            return maxAge(duration.getSeconds());
        }

        public CookieBuilder maxAge(TokenConstants tokenConstants) {
            return maxAge(tokenConstants.getTtl());
        }

        public CookieBuilder httpOnly(boolean state) {
            ensureNotBuilt();
            this.httpOnly = state;
            return this;
        }

        public CookieBuilder httpOnly() {
            return httpOnly(true);
        }

        public CookieBuilder secure(boolean state) {
            ensureNotBuilt();
            this.secure = state;
            return this;
        }

        public CookieBuilder secure() {
            return secure(true);
        }

        public CookieBuilder path(String path) {
            ensureNotBuilt();
            this.path = path;
            return this;
        }

        public CookieBuilder sameSite(SameSite scheme) {
            ensureNotBuilt();
            if (scheme == null) {
                throw new IllegalArgumentException("SameSite cannot be null");
            }
            this.sameSite = scheme;
            return this;
        }

        public ResponseCookie build(String key, String value) {
            ensureNotBuilt();
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Cookie name cannot be null or empty");
            }

            built = true;

            return ResponseCookie.from(key, value)
                    .maxAge(this.cookieAge)
                    .httpOnly(this.httpOnly)
                    .secure(this.secure)
                    .path(this.path)
                    .sameSite(this.sameSite.getScheme())
                    .build();
        }
    }

    public Optional<String> getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst()
                .map(Cookie::getValue);
    }

    public ResponseCookie createDeleteCookie(String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .maxAge(0)
                .path("/")
                .build();
    }

    public ResponseCookie createDeleteCookie(Cookie oldCookie) {
        return ResponseCookie.from(oldCookie.getName(), "")
                .maxAge(0)
                .path(oldCookie.getPath())
                .build();
    }
}