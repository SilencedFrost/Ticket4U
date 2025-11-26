package com.service;

import com.constant.SameSite;
import com.constant.TokenConstants;
import com.dto.auth.AuthResponse;
import com.dto.auth.LoginRequest;
import com.dto.auth.internal.LoginResult;
import com.entity.CustomUserDetails;
import com.nimbusds.jose.JOSEException;
import com.util.CookieUtil;
import com.util.JwtUtil;
import com.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenUtil tokenUtil;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;

    public LoginResult login(
            String email,
            String password,
            boolean rememberMe,
            String jSessionId,
            String oldRefreshToken,
            String userAgent
    ) {

        if(oldRefreshToken != null) {
            log.debug("Invalidating session token : {}", oldRefreshToken);
            sessionService.invalidate(oldRefreshToken);
        } else {
            log.debug("Old refresh token not found, skipping invalidation");
        }

        // Auth to get CustomUserDetails, will fail here if invalid credentials were provided
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(email, password));

        // Retrieve authenticated user data
        Object principal = authentication.getPrincipal();
        CustomUserDetails authenticatedUser;

        if (principal instanceof CustomUserDetails userDetails) {
            authenticatedUser = userDetails;
        } else {
            throw new IllegalStateException("Authentication principal is not UserDetails.");
        }

        // Access token generation for stateless auth on other services
        String accessToken;

        try {
            accessToken = jwtUtil.generateAuthToken(authenticatedUser, rememberMe, jSessionId);
        } catch (JOSEException e) {
            throw new RuntimeException("Token generation failed", e);
        }

        if(accessToken!= null) {
            // Build access token cookie
            ResponseCookie at = cookieUtil.builder()
                    // Enum value for normal access tokens, 1 day for non-remember tokens
                    .maxAge(rememberMe? TokenConstants.ACCESS_TOKEN.getTtl().toSeconds() : -1)
                    .sameSite(SameSite.NONE)
                    .httpOnly()
                    .secure()
                    .build(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken);

            // Delete refresh token by default
            ResponseCookie rt = cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey());

            // Build a refresh token if remember me is enabled and add to header, else ignore
            if(rememberMe) {
                String ua = userAgent != null ? userAgent : "Unknown";
                String refreshToken = tokenUtil.generateToken();
                sessionService.createSession(authenticatedUser.getUserId(), ua, refreshToken);
                rt = cookieUtil.builder()
                        .maxAge(TokenConstants.REFRESH_TOKEN)
                        .sameSite(SameSite.NONE)
                        .httpOnly()
                        .secure()
                        .build(TokenConstants.REFRESH_TOKEN.getCookieKey(), refreshToken);
            }

            return new LoginResult(
                    new AuthResponse(
                            authenticatedUser.getUserId(),
                            authenticatedUser.getRoleId(),
                            authenticatedUser.getTrueUsername()
                    ),
                    at.toString(),
                    rt.toString()
            );
        }
        throw new IllegalStateException("Access token is null");
    }

    public LoginResult login(
            LoginRequest loginRequest,
            String jSessionId,
            String oldRefreshToken,
            String userAgent
    ) {
        return login(
                loginRequest.email(),
                loginRequest.password(),
                loginRequest.rememberMe(),
                jSessionId,
                oldRefreshToken,
                userAgent);
    }
}
