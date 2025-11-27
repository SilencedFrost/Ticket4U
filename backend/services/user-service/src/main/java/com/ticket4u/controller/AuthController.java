package com.ticket4u.controller;

import com.ticket4u.constant.CommonKeys;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.service.AuthService;
import com.ticket4u.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
        String oldRefreshToken = cookieUtil.getCookie(request.getCookies(), TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
        log.debug("Old refresh token {} present", oldRefreshToken == null ? "not" : "");

        String userAgent = request.getHeader(CommonKeys.USER_AGENT.getKey());
        log.debug("User Agent: {}", userAgent);

        LoginResult loginResult = authService.login(loginRequest, oldRefreshToken, userAgent);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessTokenCookie(), loginResult.refreshTokenCookie())
                .body(loginResult.authResponse());
    }
}
