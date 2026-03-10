package com.ticket4u.util;

import com.ticket4u.constant.CommonKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpRequestUtil {

    private static final String DEFAULT_USER_AGENT = "Undefined";

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(CommonKeys.USER_AGENT.getKey());
        return userAgent != null ? userAgent : DEFAULT_USER_AGENT;
    }
}
