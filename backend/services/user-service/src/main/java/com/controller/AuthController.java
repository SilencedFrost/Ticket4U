package com.controller;

import com.dto.auth.*;
import com.dto.user.UserResponse;
import com.exception.InvalidLoginException;
import com.service.EmailService;
import com.service.SessionService;
import com.service.UserService;
import com.util.SessionCookieUtil;
import com.util.TokenGeneratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    /**
     * POST /api/auth/login
     * Validate user login and return the user DTO
     * @return User
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        if (loginRequest.rememberMe()) {
            String ua = userAgent != null ? userAgent : "Unknown";
            String sessionKey = sessionService.createSession(userResponse.userId(), ua);
            ResponseCookie sessionCookie = sessionCookieUtil.createSessionCookie(sessionKey);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                    .body(userResponse);
        }

        return ResponseEntity.ok().body(userResponse);
    }
}
