package com.ticket4u.util;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieExtratorUtil {

    private final CookieUtil cookieUtil;

    public <T extends Exception> String getRefreshTokenOrThrow(
            HttpServletRequest request,
            Supplier<? extends T> exceptionSupplier) throws T {

        return cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey())
                .orElseThrow(exceptionSupplier);
    }

    public String getRefreshTokenOrGet(HttpServletRequest request, String otherItem) {
        return cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey())
                .orElse(otherItem);

    }
}
