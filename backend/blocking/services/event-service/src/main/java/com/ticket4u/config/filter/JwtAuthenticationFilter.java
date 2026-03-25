package com.ticket4u.config.filter;

import com.nimbusds.jose.jwk.JWK;
import com.ticket4u.constants.TokenConstants;
import com.ticket4u.core.entity.CustomUserDetails;
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
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil          jwtUtil;
    private final CookieUtil       cookieUtil;
    private final AuthJwkSupplier  authJwkSupplier;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return pathMatcher.match("/api/*/public/**", path)
                || pathMatcher.match("/.well-known/**", path);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        JWK authJwk = authJwkSupplier.getJwkSafe().orElse(null);

        if (SecurityContextHolder.getContext().getAuthentication() == null
                && request.getCookies() != null
                && authJwk != null) {

            String accessToken = (String) request.getAttribute("newAccessToken");
            if (accessToken == null) {
                accessToken = cookieUtil
                        .getCookie(request.getCookies(), TokenConstants.ACCESS_TOKEN.getCookieKey())
                        .orElse(null);
            }

            if (accessToken != null && jwtUtil.validate(accessToken, authJwk)) {
                try {
                    UUID userId = UUID.fromString(jwtUtil.extractSubject(accessToken, authJwk));

                    // Roles are stored as integers in JWT — map to role name strings
                    Collection<? extends GrantedAuthority> authorities = jwtUtil.extractClaim(
                            accessToken, authJwk, claims -> {
                                try {
                                    // Try integer list first (e.g. [1, 2])
                                    Object raw = claims.getClaim("roles");
                                    if (raw instanceof List<?> list && !list.isEmpty()
                                            && list.get(0) instanceof Number) {
                                        return list.stream()
                                                .map(id -> new SimpleGrantedAuthority(
                                                        "ROLE_" + roleIdToName(((Number) id).longValue())))
                                                .toList();
                                    }
                                } catch (Exception ignored) {}
                                try {
                                    // Fallback: string list (e.g. ["EVENT_MANAGER"])
                                    List<String> roleNames = claims.getStringListClaim("roles");
                                    if (roleNames != null && !roleNames.isEmpty()) {
                                        return roleNames.stream()
                                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()))
                                                .toList();
                                    }
                                } catch (Exception ignored) {}
                                return List.<SimpleGrantedAuthority>of();
                            });

                    CustomUserDetails user = new CustomUserDetails(authorities, userId);
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Auth successful - userId: {}, authorities: {}", userId, authorities);

                } catch (Exception e) {
                    log.warn("Failed to process JWT claims: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // ── Role ID → name mapping ─────────────────────────────
    private static String roleIdToName(long roleId) {
        return switch ((int) roleId) {
            case 0  -> "CUSTOMER";
            case 1  -> "EVENT_MANAGER";
            case 2  -> "ORGANIZER_ADMIN";
            case 3  -> "ADMIN";
            case 4  -> "SYSTEM_ADMIN";
            case 10 -> "GATEKEEPER";
            case 11 -> "SUPPORT_AGENT";
            case 12 -> "FINANCE_MANAGER";
            default -> "UNKNOWN_" + roleId;
        };
    }
}