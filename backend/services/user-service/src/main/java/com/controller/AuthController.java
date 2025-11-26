package com.controller;

import com.constant.SameSite;
import com.constant.TokenConstants;
import com.dto.auth.*;
import com.entity.CustomUserDetails;
import com.nimbusds.jose.JOSEException;
import com.service.SessionService;
import com.util.JwtUtil;
import com.util.CookieUtil;
import com.util.TokenUtil;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final SessionService sessionService;
    private final CookieUtil cookieUtil;

    /**
     * POST /api/auth/login
     * Validate user login and return the user DTO
     * @return User
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            @CookieValue(value = "JSESSIONID") String jsessionid,
            @RequestHeader(value = "User-Agent") String userAgent
    ) {
        // Auth
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        // Put in context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Retrieve user data
        Object principal = authentication.getPrincipal();
        CustomUserDetails authenticatedUser;

        if (principal instanceof CustomUserDetails userDetails) {
            authenticatedUser = userDetails;
        } else {
            throw new IllegalStateException("Authentication principal is not UserDetails.");
        }

        // TokenConstants & session generation
        String accessToken;

        // Access token generation
        try {
            accessToken = jwtUtil.generateAuthToken(authenticatedUser, loginRequest.rememberMe(), jsessionid);
        } catch (JOSEException e) {
            throw new RuntimeException("Token generation failed", e);
        }

        if(accessToken!= null) {
            // Start building response
            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();

            // Build access token cookie
            ResponseCookie at = cookieUtil.builder()
                    .maxAge(TokenConstants.ACCESS_TOKEN)
                    .sameSite(SameSite.NONE)
                    .httpOnly()
                    .secure()
                    .build(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken);

            // Build a refresh token if yes and add to header, else ignore
            if(loginRequest.rememberMe()) {
                String ua = userAgent != null ? userAgent : "Unknown";
                String refreshToken = tokenUtil.generateToken();
                sessionService.createSession(authenticatedUser.getUserId(), ua, refreshToken);
                ResponseCookie rt = cookieUtil.builder()
                        .maxAge(TokenConstants.REFRESH_TOKEN)
                        .sameSite(SameSite.NONE)
                        .httpOnly()
                        .secure()
                        .build(TokenConstants.REFRESH_TOKEN.getCookieKey(), refreshToken);

                responseBuilder.header(HttpHeaders.SET_COOKIE, at.toString(), rt.toString());
            } else {
                responseBuilder.header(HttpHeaders.SET_COOKIE, at.toString());
            }

            // Build and return
            return responseBuilder.body(
                    new AuthResponse(
                            authenticatedUser.getUserId(),
                            authenticatedUser.getRoleId(),
                            authenticatedUser.getTrueUsername()
                    )
            );
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
