package com.ticket4u.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    public Optional<String> getCookie(Cookie[] cookies, String name) {
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}