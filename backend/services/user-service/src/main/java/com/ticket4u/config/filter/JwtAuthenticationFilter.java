package com.ticket4u.config.filter;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.AuthService;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

        // Skip filter for public endpoints
        if (pathMatcher.match("/api/*/public/**", path)) {
            return true;
        }

        // Skip filter for login endpoint
        if (pathMatcher.match("/api/*/auth/login", path)) {
            return true;
        }

        // Skip filter for logout endpoint
        return pathMatcher.match("/api/*/auth/logout", path);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null && request.getCookies() != null) {

            String accessToken = (String) request.getAttribute("newAccessToken");

            if (accessToken == null) {
                accessToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey()).orElse(null);
            }

            // If access token is there and valid (original or refreshed)
            if(accessToken != null && jwtUtil.validate(accessToken)) {
                try {
                    UUID userId = UUID.fromString(jwtUtil.extractSubject(accessToken));

                    Collection<? extends GrantedAuthority> authorities = List.of();

                    // Extract and convert roles to Spring Security Authorities
                    List<String> roles = jwtUtil.extractClaim(accessToken, claims -> {
                        try {
                            return claims.getStringListClaim("roles");
                        } catch (ParseException e) {
                            return List.of();
                        }
                    });

                    if(!roles.isEmpty()) {
                        authorities = roles.stream()
                                .map(role -> "ROLE_" + role.toUpperCase())
                                .map(SimpleGrantedAuthority::new)
                                .toList();
                    }

                    // Construct CustomUserDetails, put it in Security Context
                    CustomUserDetails user = new CustomUserDetails(authorities, userId);

                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    log.info("Failed to process valid Access Token claims: ", e);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
