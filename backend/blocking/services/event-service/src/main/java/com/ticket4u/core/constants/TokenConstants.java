package com.ticket4u.core.constants;

import lombok.Getter;

import java.time.Duration;

@Getter
public enum TokenConstants {
    REFRESH_TOKEN("rt", Duration.ofDays(60), Duration.ofDays(7)),
    ACCESS_TOKEN("at", Duration.ofMinutes(15), null);

    private final String cookieKey;
    private final Duration absoluteTTL;
    private final Duration rollingTTL;

    TokenConstants(String cookieKey, Duration absoluteTTL, Duration rollingTTL) {
        this.cookieKey = cookieKey;
        this.absoluteTTL = absoluteTTL;
        this.rollingTTL = rollingTTL;
    }
}