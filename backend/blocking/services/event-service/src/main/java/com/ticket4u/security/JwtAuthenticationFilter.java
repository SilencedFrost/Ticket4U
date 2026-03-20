package com.ticket4u.security;

import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.core.constants.TokenConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return pathMatcher.match("/api/*/public/**", path)
                || pathMatcher.match("/.well-known/**", path)
                || pathMatcher.match("/api/*/auth/**", path);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null
                && request.getCookies() != null) {

            String accessToken = cookieUtil
                    .getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey())
                    .orElse(null);

            if (accessToken != null && jwtUtil.validate(accessToken)) {
                try {
                    String userId = jwtUtil.extractSubject(accessToken);

                    List<String> roles = jwtUtil.extractClaim(accessToken, claims -> {
                        try {
                            return claims.getStringListClaim("roles");
                        } catch (ParseException e) {
                            return List.of();
                        }
                    });

                    var authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .toList();

                    var auth = new UsernamePasswordAuthenticationToken(
                            userId, null, authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);

                } catch (Exception e) {
                    log.warn("Failed to process JWT claims: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}