package com.ticket4u.controller;

import com.ticket4u.constant.CommonKeys;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.*;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.exception.UnauthorizedException;
import com.ticket4u.service.AuthService;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final CookieUtil cookieUtil;
    private final AuthService authService;

    /**
     * POST /api/v1/auth/refresh
     * Validate user session via refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest request
    ) {
        String refreshToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey())
                .orElseThrow(() -> new UnauthorizedException("No refresh token provided"));

        RefreshResult refreshResult = authService.refresh(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshResult.accessTokenCookie(), refreshResult.refreshTokenCookie())
                .body(refreshResult.authResponse());
    }

    /**
     * POST /api/v1/auth/login
     * Validate user login and return the user DTO
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        log.debug("Login request received");
        // Cookie and header extraction, ready for fresh auth
        String oldRefreshToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
        log.debug("Old refresh token {} present", oldRefreshToken == null ? "not" : "");

        String userAgent = request.getHeader(CommonKeys.USER_AGENT.getKey());
        String ua = userAgent != null? userAgent : "Undefined";
        log.debug("User Agent: {}", ua);

        LoginResult loginResult = authService.login(loginRequest, oldRefreshToken, ua);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessTokenCookie(), loginResult.refreshTokenCookie())
                .body(loginResult.authResponse());
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String oldRefreshToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
        try {
            LogoutResult logoutResult = authService.logout(oldRefreshToken);
            // Send delete cookies if logout succeeded
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, logoutResult.accessTokenCookie(), logoutResult.refreshTokenCookie()).build();

        } catch (Exception e) {
            log.error("Logout failed - session not invalidated", e);
            // Return error so frontend keeps user logged in
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Logout failed. Please try again.");
        }
    }

    /**
     * POST /api/v1/auth/register
     * Register new user with email and password
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerWithEmail(request));
    }

    /**
     * POST /api/v1/auth/register/google
     * Register new user with Google OAuth2
     */
    @PostMapping("/register/google")
    public ResponseEntity<RegisterResponse> registerWithGoogle(@Valid @RequestBody OAuth2RegisterRequest request) {
        return ResponseEntity.ok(authService.registerWithGoogle(request.idToken()));
    }
}
