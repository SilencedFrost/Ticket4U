package com.ticket4u.config.filter;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.constants.TokenConstants;
import com.ticket4u.core.entity.CustomUserDetails;
import com.ticket4u.jwk.supplier.EventJwkSupplier;
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

    private final JwtUtil          jwtUtil;
    private final CookieUtil       cookieUtil;
    private final EventJwkSupplier eventJwkSupplier;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return pathMatcher.match("/api/v1/public/**", path)
                || pathMatcher.match("/.well-known/**", path);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        JWK jwk = eventJwkSupplier.getJwkSafe().orElse(null);

        if (SecurityContextHolder.getContext().getAuthentication() == null
                && request.getCookies() != null
                && jwk != null) {

            String accessToken = (String) request.getAttribute("newAccessToken");
            if (accessToken == null) {
                accessToken = cookieUtil
                        .getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey())
                        .orElse(null);
            }
            try {
                com.nimbusds.jwt.SignedJWT jwt = com.nimbusds.jwt.SignedJWT.parse(accessToken);
                log.info("Token alg: {}, kid: {}", jwt.getHeader().getAlgorithm(), jwt.getHeader().getKeyID());
            } catch (Exception e) {
                log.warn("Could not parse token header: {}", e.getMessage());
            }
            if (accessToken != null && jwtUtil.validate(accessToken, jwk)) {
                try {
                    UUID userId = UUID.fromString(jwtUtil.extractSubject(accessToken, jwk));

                    List<String> roles = jwtUtil.extractClaim(accessToken, jwk, claims -> {
                        try {
                            return claims.getStringListClaim("roles");
                        } catch (ParseException e) {
                            return List.of();
                        }
                    });

                    Collection<? extends GrantedAuthority> authorities = roles.isEmpty()
                            ? List.of()
                            : roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .toList();

                    CustomUserDetails user = new CustomUserDetails(authorities, userId);
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);

                } catch (Exception e) {
                    log.warn("Failed to process JWT claims: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}