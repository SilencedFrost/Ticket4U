package com.ticket4u.config.filter;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.jwk.supplier.AuthJwkSupplier;
import com.ticket4u.jwk.util.JwtUtil;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AuthJwkSupplier authJwkSupplier;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip filter for public endpoints
        return pathMatcher.match("/api/*/public/**", path) || pathMatcher.match("/api/*/internal/**", path);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        JWK authJwk = authJwkSupplier.getJwkSafe().orElse(null);

        if(SecurityContextHolder.getContext().getAuthentication() == null && request.getCookies() != null && authJwk != null) {

            String accessToken = (String) request.getAttribute("newAccessToken");

            if (accessToken == null) {
                accessToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey()).orElse(null);
            }

            // If access token is there and valid (original or refreshed)
            if(accessToken != null && jwtUtil.validate(accessToken, authJwk)) {
                try {
                    UUID userId = UUID.fromString(jwtUtil.extractSubject(accessToken, authJwk));

                    Collection<? extends GrantedAuthority> authorities = List.of();

                    // Extract and convert roles to Spring Security Authorities
                    List<String> roles = jwtUtil.extractClaim(accessToken, authJwk,claims -> {
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
