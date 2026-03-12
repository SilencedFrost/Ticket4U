package com.ticket4u.controller;

import com.ticket4u.dto.auth.*;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.exception.UnauthorizedException;
import com.ticket4u.service.AuthService;
import com.ticket4u.service.EmailVerificationService;
import com.ticket4u.service.PasswordResetService;
import com.ticket4u.util.CookieExtratorUtil;
import com.ticket4u.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final CookieExtratorUtil cookieExtratorUtil;
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    /**
     * POST /api/v1/auth/refresh
     * Validate user session via refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request
    ) {
        String refreshToken = cookieExtratorUtil.getRefreshTokenOrThrow(request, () -> new UnauthorizedException("No refresh token provided"));

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
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        String oldRefreshToken = cookieExtratorUtil.getRefreshTokenOrGet(request, null);

        String ua = HttpRequestUtil.getUserAgent(request);

        LoginResult loginResult = authService.login(loginRequest, oldRefreshToken, ua);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessTokenCookie(), loginResult.refreshTokenCookie())
                .body(loginResult.authResponse());
    }

    /**
     * POST /api/v1/auth/logout
     * Validate invalidate user session
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String refreshToken = cookieExtratorUtil.getRefreshTokenOrThrow(request, () -> new UnauthorizedException("No refresh token provided"));

        LogoutResult logoutResult = authService.logout(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, logoutResult.accessTokenCookie(), logoutResult.refreshTokenCookie())
                .build();
    }

    /**
     * POST /api/v1/auth/register
     * Register new user with email and password
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerWithEmail(request));
    }

    /**
     * POST /api/v1/auth/google
     * Find or create user with Google OAuth2, then return session tokens
     */
    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(
            @Valid @RequestBody OAuth2RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        String ua = HttpRequestUtil.getUserAgent(httpRequest);

        LoginResult loginResult = authService.authenticateWithGoogle(request.idToken(), ua);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessTokenCookie(), loginResult.refreshTokenCookie())
                .body(loginResult.authResponse());
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyToken(token);
        return ResponseEntity.ok(Map.of("message", "auth.verification.success"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendVerification(request.email());
        return ResponseEntity.ok(Map.of("message", "auth.verification.check_email"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendPasswordResetEmail(request.email());
        return ResponseEntity.ok(Map.of("message", "auth.password_reset.check_email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.password());
        return ResponseEntity.ok(Map.of("message", "auth.password_reset.success"));
    }
}
