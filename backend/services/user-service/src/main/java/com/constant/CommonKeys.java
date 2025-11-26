package com.constant;

import lombok.Getter;

@Getter
public enum CommonKeys {
    JSESSIONID("JSESSIONID"),
    USER_AGENT("User-Agent");

    private final String key;

    private CommonKeys(String key) {
        this.key = key;
    }
}
