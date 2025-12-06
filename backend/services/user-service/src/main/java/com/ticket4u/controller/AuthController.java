package com.ticket4u.controller;

import com.ticket4u.constant.CommonKeys;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.service.AuthService;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String oldRefreshToken = cookieUtil.getCookieBody(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
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
        // Get old at and rt
        Cookie[] cookies = request.getCookies();
        Cookie oldRefreshTokenCookie = cookieUtil.getCookie(cookies, TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
        Cookie oldAccessTokenCookie = cookieUtil.getCookie(cookies, TokenConstants.ACCESS_TOKEN.getCookieKey()).orElse(null);

        try {
            // Pass old at and rt into authService.logout, get back the at and rt delete cookie
            LogoutResult logoutResult = authService.logout(oldAccessTokenCookie, oldRefreshTokenCookie);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, logoutResult.accessTokenCookie(), logoutResult.refreshTokenCookie()).build();
        } catch (Exception e) {
            log.error("Logout failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
