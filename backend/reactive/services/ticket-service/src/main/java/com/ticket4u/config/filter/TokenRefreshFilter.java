package com.ticket4u.config.filter;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.jwk.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRefreshFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip filter for public endpoints
        return (pathMatcher.match("/api/*/public/**", path));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null && request.getCookies() != null) {
            // Get tokens
            String accessToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey()).orElse(null);
            String refreshToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);

            boolean isAccessTokenValid = accessToken != null && jwtUtil.validate(accessToken);

            // If access token is not there, or invalid, but refresh token is there
            if (!isAccessTokenValid && refreshToken != null) {
                //TODO: Internal API call to user service for silent refresh
            }
        }
        filterChain.doFilter(request, response);
    }
}

