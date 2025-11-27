package com.ticket4u.constant;

import lombok.Getter;

import java.time.Duration;

@Getter
public enum TokenConstants {
    REFRESH_TOKEN("rt", Duration.ofDays(90)),
    ACCESS_TOKEN("at", Duration.ofMinutes(15));

    private final String cookieKey;
    private final Duration ttl;

    private TokenConstants(String cookieKey, Duration ttl) {
        this.cookieKey = cookieKey;
        this.ttl = ttl;
    }
}
