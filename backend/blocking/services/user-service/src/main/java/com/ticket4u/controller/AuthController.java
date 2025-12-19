package com.ticket4u.controller;

import com.ticket4u.constant.CommonKeys;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.*;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.service.AuthService;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
     * POST /api/auth/login
     * Validate user login and return the user DTO
     * @return User
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

    /**
     * POST /api/v1/auth/register
     * Register new user with email and password
     * @return RegisterResponse
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        log.debug("Register request received for email: {}", registerRequest.email());
        RegisterResponse response = authService.registerWithEmail(registerRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/auth/register/google
     * Register new user with Google OAuth2
     * @return RegisterResponse
     */
    @PostMapping("/register/google")
    public ResponseEntity<RegisterResponse> registerWithGoogle(
            @Valid @RequestBody OAuth2RegisterRequest oAuth2RegisterRequest
    ) {
        log.debug("Google register request received");
        RegisterResponse response = authService.registerWithGoogle(oAuth2RegisterRequest);
        return ResponseEntity.ok(response);
    }
}
