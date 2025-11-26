package com.controller;

import com.constant.CommonKeys;
import com.constant.TokenConstants;
import com.dto.auth.AuthResponse;
import com.dto.auth.LoginRequest;
import com.dto.auth.internal.LoginResult;
import com.exception.JSessionIdNotFoundException;
import com.service.AuthService;
import com.util.CookieUtil;
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
        String jSessionId = cookieUtil.getCookie(request, CommonKeys.JSESSIONID.getKey()).orElseThrow(() -> new JSessionIdNotFoundException("JSESSIONID not found!"));
        String oldRefreshToken = cookieUtil.getCookie(request, TokenConstants.REFRESH_TOKEN.getCookieKey()).orElse(null);
        log.debug("JSESSIONID and old refresh token {} present", oldRefreshToken == null ? "not" : "");

        String userAgent = request.getHeader(CommonKeys.USER_AGENT.getKey());
        log.debug("User Agent: {}", userAgent);

        LoginResult loginResult = authService.login(loginRequest, jSessionId, oldRefreshToken, userAgent);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessTokenCookie(), loginResult.accessTokenCookie())
                .body(loginResult.authResponse());
    }
}
