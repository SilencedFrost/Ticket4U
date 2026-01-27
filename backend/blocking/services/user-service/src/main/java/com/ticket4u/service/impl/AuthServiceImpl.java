package com.ticket4u.service.impl;

import com.nimbusds.jose.JOSEException;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.GoogleUserInfo;
import com.ticket4u.dto.auth.GoogleIdToken;
import com.ticket4u.dto.auth.LoginRequest;
import com.ticket4u.dto.auth.RegisterRequest;
import com.ticket4u.dto.auth.RegisterResponse;
import com.ticket4u.dto.auth.AuthResponse;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.entity.User;
import com.ticket4u.entity.Role;
import com.ticket4u.exception.EmailAlreadyExistException;
import com.ticket4u.exception.LogoutFailedException;
import com.ticket4u.exception.PhoneNumberAlreadyExistException;
import com.ticket4u.exception.TokenCreationException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.AuthService;
import com.ticket4u.service.SessionService;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.PhoneNumberUtil;
import com.ticket4u.util.TokenUtil;
import com.ticket4u.validation.GoogleTokenValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final GoogleTokenValidator googleTokenValidator;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public LoginResult login(
            @Valid LoginRequest loginRequest,
            String oldRefreshToken,
            String userAgent
    ) {
        // Get CustomUserDetails, will fail here if invalid credentials were provided
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        // Retrieve authenticated user data
        Object principal = authentication.getPrincipal();
        CustomUserDetails authenticatedUser;

        if (principal instanceof CustomUserDetails userDetails) {
            authenticatedUser = userDetails;
        } else {
            throw new TokenCreationException("Authentication principal is not UserDetails.");
        }

        log.info("Authentication for user {} successful", authenticatedUser.getTrueUsername());

        // Create access token
        Optional<String> accessToken = createAccessToken(authenticatedUser);

        if (accessToken.isEmpty()) {
            throw new TokenCreationException("Failed to create access token");
        }

        String at = createAccessTokenCookie(accessToken.get());

        String refreshToken = tokenUtil.generateToken();
        String rt = createRefreshTokenCookie(refreshToken, loginRequest.rememberMe());

        // Create session object
        sessionService.createSession(authenticatedUser.getUserId(), userAgent, refreshToken, loginRequest.rememberMe());

        if (oldRefreshToken != null) {
            try {
                log.debug("Invalidating session token with hash: {}", DigestUtils.sha256Hex(oldRefreshToken));
                sessionService.invalidate(oldRefreshToken);
            } catch (Exception e) {
                log.warn("Failed to invalidate old refresh token, continuing anyway", e);
            }
        }

        return new LoginResult(
                new AuthResponse(
                        authenticatedUser.getUserId(),
                        authenticatedUser.getRoleId(),
                        authenticatedUser.getTrueUsername(),
                        // Email is mapped to Spring's username field
                        authenticatedUser.getUsername()
                ),
                at,
                rt
        );
    }

    @Transactional
    public LogoutResult logout(String refreshToken) {
        // Validate refresh token exists
        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("No refresh token provided for logout");
            throw new LogoutFailedException("No refresh token provided", "error.logout.invalidation_failed");
        }
        log.debug("Invalidating refresh token session");
        sessionService.invalidate(refreshToken);
        log.info("Session has been invalidated successfully");

        // Create delete cookies after successful invalidation
        ResponseCookie atDeleteCookie = cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey());
        ResponseCookie rtDeleteCookie = cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey());

        return new LogoutResult(atDeleteCookie.toString(), rtDeleteCookie.toString());
    }



    @Override
    @Transactional
    public RefreshResult refresh(String refreshToken) {
        RefreshCreationResult refreshCreationResult = sessionService.refresh(refreshToken);
        UserResponse userResponse = refreshCreationResult.userResponse();

        if (userResponse == null) {
            log.error("Refresh returned null user response");
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        }

        Optional<String> accessToken = createAccessToken(userResponse);

        if (accessToken.isEmpty()) {
            log.error("Failed to create access token during refresh for user {}", userResponse.id());
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        }

        String accessTokenCookie = createAccessTokenCookie(accessToken.get());

        return new RefreshResult(
                accessTokenCookie,
                accessToken.get(),
                refreshCreationResult.refreshToken()
        );
    }

    // Helper methods
    private Optional<String> createAccessToken(CustomUserDetails userDetails) {
        try {
            String token = jwtUtil.generateAuthToken(userDetails);
            return token != null ? Optional.of(token) : Optional.empty();
        } catch (JOSEException e) {
            log.error("Token generation failed", e);
            return Optional.empty();
        }
    }

    private Optional<String> createAccessToken(UserResponse userResponse) {
        return createAccessToken(new CustomUserDetails(
                userResponse.email(),
                null,
                List.of(new SimpleGrantedAuthority(userResponse.role())),
                userResponse.id(),
                userResponse.roleId(),
                userResponse.username()
        ));
    }

    private RefreshResult createDeleteCookiesResult() {
        return new RefreshResult(
                cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey()).toString(),
                null,
                cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey()).toString()
        );
    }

    private String createAccessTokenCookie(String accessToken) {
        ResponseCookie cookie = CookieUtil.secureBuilder(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken)
                .maxAge(TokenConstants.ACCESS_TOKEN.getAbsoluteTTL())
                .build();
        return cookie.toString();
    }

    private String createRefreshTokenCookie(String refreshToken, boolean rememberMe) {
        Duration ttl = rememberMe ? TokenConstants.REFRESH_TOKEN.getRollingTTL() : Duration.ofDays(1);
        ResponseCookie.ResponseCookieBuilder rtBuilder = CookieUtil.secureBuilder(
                TokenConstants.REFRESH_TOKEN.getCookieKey(),
                refreshToken
        );
        return rtBuilder.maxAge(ttl).build().toString();
    }



    private String[] splitFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new String[]{null, null};
        }
        String[] names = fullName.trim().split("\\s+", 2);
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : null;
        return new String[]{firstName, lastName};
    }

    @Override
    @Transactional
    public RegisterResponse registerWithEmail(@Valid RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            return new RegisterResponse(
                null,
                request.email(),
                "user.registration.check_email"
            );
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            String normalizedPhone = PhoneNumberUtil.normalize(request.phoneNumber());
            if (userRepository.existsByPhoneNumber(normalizedPhone)) {
                return new RegisterResponse(
                    null,
                    request.email(),
                   "user.registration.check_email"
                );
            }
        }

        User user = userMapper.toEntityFromRegister(request);
        user.assignRole(new Role() {{ setId(0); }});
        User savedUser = userRepository.save(user);

        // TODO: Send verification email

        log.info("User registered successfully with email: {}, userId: {}", savedUser.getEmail(), savedUser.getId());
        return userMapper.toRegisterResponse(savedUser, "user.registration.check_email");
    }


    @Override
    @Transactional
    public RegisterResponse registerWithGoogle(String idTokenValue) {
        GoogleIdToken idToken = new GoogleIdToken(idTokenValue);
        GoogleUserInfo userInfo = googleTokenValidator.verifyAndExtract(idToken);

        if (userRepository.existsByEmailIgnoreCase(userInfo.email())) {
            log.warn("Google registration attempt with existing email: {}", userInfo.email());
            return new RegisterResponse(
                null,
                userInfo.email(),
                "auth.login.try_login"
            );
        }

        User user = userMapper.toEntityFromGoogle(userInfo);
        user.assignRole(new Role() {{ setId(0); }});
        User savedUser = userRepository.save(user);
        log.info("User registered via Google successfully: {}, userId: {}", savedUser.getEmail(), savedUser.getId());

        return userMapper.toRegisterResponse(savedUser, "auth.login.try_login");
    }
}
