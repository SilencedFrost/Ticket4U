package com.ticket4u.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.Option;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.exception.TokenCreationException;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.TokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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

    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResult login(
            @Valid LoginRequest loginRequest,
            String oldRefreshToken,
            String userAgent
    ) {
        // Get CustomUserDetails, will fail here if invalid credentials were provided
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

        // Create access token
        Optional<String> accessToken = createAccessToken(authenticatedUser);

        if(accessToken.isEmpty()) {
            throw new TokenCreationException("Failed to create access token");
        }

        String at = createAccessTokenCookie(accessToken.get());

        String refreshToken = tokenUtil.generateToken();
        String rt = createRefreshTokenCookie(refreshToken, loginRequest.rememberMe());

        // Create session object
        sessionService.createSession(authenticatedUser.getUserId(), userAgent, refreshToken, loginRequest.rememberMe());

        if(oldRefreshToken != null) {
            try {
                log.debug("Invalidating session token with hash: {}", DigestUtils.sha256Hex(oldRefreshToken));
                sessionService.invalidate(oldRefreshToken);
            } catch (Exception e) {
                log.warn("Failed to invalidate old refresh token, continuing anyway", e);
            }
        }

        return new LoginResult(
                new AuthResponse(
                        authenticatedUser.getUserId(),
                        authenticatedUser.getRoleId(),
                        authenticatedUser.getTrueUsername(),
                        // Email is mapped to Spring's username field
                        authenticatedUser.getUsername()
                ),
                at,
                rt
        );
    }

    @Transactional
    public RefreshResult refresh(String refreshToken) {
        RefreshCreationResult refreshCreationResult = sessionService.refresh(refreshToken);
        UserResponse userResponse = refreshCreationResult.userResponse();

        if (userResponse == null) {
            log.error("Refresh returned null user response");
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        }

        Optional<String> accessToken = createAccessToken(userResponse);

        if(accessToken.isEmpty()) {
            log.error("Failed to create access token during refresh for user {}", userResponse.id());
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        }

        String accessTokenCookie = createAccessTokenCookie(accessToken.get());

        return new RefreshResult(
                accessTokenCookie,
                accessToken.get(),
                refreshCreationResult.refreshToken()
        );
    }

    private Optional<String> createAccessToken(CustomUserDetails userDetails) {
        try {
            String token = jwtUtil.generateAuthToken(userDetails);
            return token != null ? Optional.of(token) : Optional.empty();
        } catch (JOSEException e) {
            log.error("Token generation failed", e);
            return Optional.empty();
        }
    }

    private Optional<String> createAccessToken(UserResponse userResponse) {
        return createAccessToken(new CustomUserDetails(
                userResponse.email(),
                null,
                List.of(new SimpleGrantedAuthority(userResponse.role())),
                userResponse.id(),
                userResponse.roleId(),
                userResponse.username()
        ));
    }

    private String createAccessTokenCookie(String accessToken) {
        if(accessToken.isEmpty()) {
            throw new TokenCreationException("Access token is empty");
        }
        // Build access token cookie
        ResponseCookie at = CookieUtil.secureBuilder(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken)
                .maxAge(TokenConstants.ACCESS_TOKEN.getAbsoluteTTL())
                .build();
        return at.toString();
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
}
