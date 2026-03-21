package com.ticket4u.constants;

import lombok.Getter;

@Getter
public enum SameSite {
    NONE("None"),
    LAX("Lax"),
    STRICT("Strict");

    private final String scheme;

    private SameSite(String scheme) {
        this.scheme = scheme;
    }
}
