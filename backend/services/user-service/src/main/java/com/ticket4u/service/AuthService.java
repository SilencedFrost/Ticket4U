package com.ticket4u.service;

import com.nimbusds.jose.JOSEException;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.exception.TokenCreationException;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil;

    @Transactional
    public LoginResult login(
            @Valid LoginRequest loginRequest,
            String oldRefreshToken,
            String userAgent
    ) {
        // Auth to get CustomUserDetails, will fail here if invalid credentials were provided
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        // Retrieve authenticated user data
        Object principal = authentication.getPrincipal();
        CustomUserDetails authenticatedUser;

        if (principal instanceof CustomUserDetails userDetails) {
            authenticatedUser = userDetails;
        } else {
            throw new TokenCreationException("Authentication principal is not UserDetails.");
        }

        log.info("Authentication for user {} successful", authenticatedUser.getTrueUsername());

        Optional<String> at = createAccessTokenCookie(createAccessToken(authenticatedUser));

        if(at.isEmpty()) throw new TokenCreationException("Access token is null");

        String refreshToken = tokenUtil.generateToken();

        String rt = createRefreshTokenCookie(refreshToken, loginRequest.rememberMe());

        // Create session object
        sessionService.createSession(authenticatedUser.getUserId(), userAgent, refreshToken, parseRefreshTokenTTL(loginRequest.rememberMe()));

        if(oldRefreshToken != null) {
            log.debug("Invalidating session token : {}", oldRefreshToken);
            sessionService.invalidate(oldRefreshToken);
        } else {
            log.debug("Old refresh token not found, skipping invalidation");
        }

        return new LoginResult(
                new AuthResponse(
                        authenticatedUser.getUserId(),
                        authenticatedUser.getRoleId(),
                        authenticatedUser.getTrueUsername(),
                        // Email is mapped to Spring's username field
                        authenticatedUser.getUsername()
                ),
                at.get(),
                rt
        );
    }

    @Transactional
    public RefreshResult refresh(String refreshToken) {
        RefreshCreationResult refreshCreationResult = sessionService.refresh(refreshToken);

        UserResponse userResponse = refreshCreationResult.userResponse();

        String accessToken = createAccessToken(userResponse);
        Optional<String> accessTokenCookie = createAccessTokenCookie(accessToken);

        if(accessTokenCookie.isEmpty()) {
            log.error("Failed to create access token during refresh for user {}", userResponse.id());
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        } else {
            return new RefreshResult(
                    accessTokenCookie.get(),
                    accessToken,
                    refreshCreationResult.refreshToken()
            );
        }
    }

    private String createAccessToken(CustomUserDetails userDetails) {
        try {
            return jwtUtil.generateAuthToken(userDetails);
        } catch (JOSEException e) {
            throw new TokenCreationException("Token generation failed");
        }

    }

    private String createAccessToken(UserResponse userResponse) {
        return createAccessToken(new CustomUserDetails(
                userResponse.email(),
                null,
                List.of(new SimpleGrantedAuthority(userResponse.role())),
                userResponse.id(),
                userResponse.roleId(),
                userResponse.username()
        ));
    }

    private Optional<String> createAccessTokenCookie(String accessToken) {
        // Build access token cookie
        ResponseCookie at = CookieUtil.secureBuilder(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken)
                .maxAge(TokenConstants.ACCESS_TOKEN.getAbsoluteTTL())
                .build();
        return Optional.of(at.toString());
    }

    private static String createRefreshTokenCookie(String refreshToken, boolean rememberMe) {
        // normal if remember me, else only one day
        return createRefreshTokenCookie(refreshToken, parseRefreshTokenTTL(rememberMe));
    }

    private static Duration parseRefreshTokenTTL(boolean rememberMe) {
        return rememberMe? TokenConstants.REFRESH_TOKEN.getRollingTTL() : Duration.ofDays(1);
    }

    private static String createRefreshTokenCookie(String refreshToken, Duration ttl) {
        ResponseCookie.ResponseCookieBuilder rtBuilder = CookieUtil.secureBuilder(TokenConstants.REFRESH_TOKEN.getCookieKey(), refreshToken);

        return rtBuilder.maxAge(ttl).build().toString();
    }

    private RefreshResult createDeleteCookiesResult() {
        return new RefreshResult(
                cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey()).toString(),
                null,
                cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey()).toString()
        );
    }
    @Transactional
    public LogoutResult logout(Cookie accessTokenCookie, Cookie refreshTokenCookie) {
        // if access token null, ignore
        if (accessTokenCookie == null) {
            log.error("Access token cookie is null, ignoring logout request ");
            return new LogoutResult("", "");
        }
        // generate delete at and rt cookies with cookie util to match all attributes
        ResponseCookie atDeleteCookie = cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey());
        ResponseCookie rtDeleteCookie = cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey());

        // get refresh token value from cookie for session invalidation
        String refreshToken = refreshTokenCookie.getValue();

        // call session service invalidate, if succeed, return, else throw an error
        if (refreshToken != null && refreshToken.isBlank()) {
            log.debug("Refresh token is blank");
            try {
                sessionService.invalidate(refreshToken);
                log.info("Session has been invalidated successfully");
            } catch (Exception e) {
                log.error("Invalidate session failed", e);
                // trigger rollback
                throw new RuntimeException("Logout failed: Could not invalidate session", e);
            }
        }
        return new LogoutResult(atDeleteCookie.toString(), rtDeleteCookie.toString());
    }
}
