package com.ticket4u.service;

import com.nimbusds.jose.JOSEException;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.TokenUtil;
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

    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;

    public LoginResult login(
            String email,
            String password,
            boolean rememberMe,
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

        log.info("Authentication for user {} successfully", authenticatedUser.getTrueUsername());

        // Access token generation for stateless auth on other services
        String accessToken;

        try {
            accessToken = jwtUtil.generateAuthToken(authenticatedUser);
        } catch (JOSEException e) {
            throw new RuntimeException("Token generation failed", e);
        }

        if(accessToken!= null) {

            // Build access token cookie
            ResponseCookie at = CookieUtil.secureBuilder(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken)
                    .maxAge(TokenConstants.ACCESS_TOKEN.getTtl())
                    .build();

            // Build refresh token
            String ua = userAgent != null ? userAgent : "Unknown";

            String refreshToken = tokenUtil.generateToken();

            ResponseCookie.ResponseCookieBuilder rtBuilder = CookieUtil.secureBuilder(TokenConstants.REFRESH_TOKEN.getCookieKey(), refreshToken);

            if(rememberMe) rtBuilder.maxAge(TokenConstants.REFRESH_TOKEN.getTtl());

            ResponseCookie rt = rtBuilder.build();

            // Create session object
            sessionService.createSession(authenticatedUser.getUserId(), ua, refreshToken, rememberMe? TokenConstants.REFRESH_TOKEN.getTtl() : Duration.ofDays(1));

            return new LoginResult(
                    new AuthResponse(
                            authenticatedUser.getUserId(),
                            authenticatedUser.getRoleId(),
                            authenticatedUser.getTrueUsername(),
                            // Email is mapped to Spring's username field
                            authenticatedUser.getUsername()
                    ),
                    at.toString(),
                    rt.toString()
            );
        }
        throw new IllegalStateException("Access token is null");
    }

    public LoginResult login(
            LoginRequest loginRequest,
            String oldRefreshToken,
            String userAgent
    ) {
        return login(
                loginRequest.email(),
                loginRequest.password(),
                loginRequest.rememberMe(),
                oldRefreshToken,
                userAgent);
    }
}
