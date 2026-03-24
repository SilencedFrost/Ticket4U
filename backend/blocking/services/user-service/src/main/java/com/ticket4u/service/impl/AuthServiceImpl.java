package com.ticket4u.service.impl;

import com.nimbusds.jose.JOSEException;
import com.ticket4u.constant.RoleId;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.constant.TokenType;
import com.ticket4u.dto.auth.*;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.LogoutResult;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.entity.Role;
import com.ticket4u.entity.User;
import com.ticket4u.exception.TokenCreationException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.AuthService;
import com.ticket4u.service.EmailVerificationService;
import com.ticket4u.service.SessionService;
import com.ticket4u.service.VerificationTokenService;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.PhoneNumberUtil;
import com.ticket4u.util.TokenUtil;
import com.ticket4u.validation.GoogleTokenValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final GoogleTokenValidator googleTokenValidator;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;
    private final VerificationTokenService verificationTokenService;

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

    @Override
    @Transactional
    public LogoutResult logout(String refreshToken) {
        sessionService.invalidate(refreshToken);

        return new LogoutResult(
                cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey()).toString(),
                cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey()).toString()
        );
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
        String refreshTokenCookie = createRefreshTokenCookie(refreshCreationResult.refreshToken(), refreshCreationResult.rememberMe());

        return new RefreshResult(
                accessTokenCookie,
                accessToken.get(),
                refreshTokenCookie,
                new AuthResponse(
                        userResponse.id(),
                        userResponse.roleId(),
                        userResponse.username(),
                        userResponse.email()
                )
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
                cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey()).toString(),
                null
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

    @Override
    @Transactional
    public RegisterResponse registerWithEmail(@Valid RegisterRequest request) {
        // TODO: add synthetic delay based on last N delay observed by mail service with variance to eliminate timing attacks
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
        user.assignRole(new Role() {{ setId(RoleId.CUSTOMER); }});
        User savedUser = userRepository.save(user);

        emailVerificationService.sendVerificationEmail(savedUser);

        log.info("User registered successfully with email: {}, userId: {}", savedUser.getEmail(), savedUser.getId());
        return userMapper.toRegisterResponse(savedUser, "user.registration.check_email");
    }


    @Override
    @Transactional
    public LoginResult authenticateWithGoogle(String idTokenValue, String userAgent) {
        GoogleIdToken idToken = new GoogleIdToken(idTokenValue);
        GoogleUserInfo userInfo = googleTokenValidator.verifyAndExtract(idToken);

        if (!userRepository.existsByEmailIgnoreCase(userInfo.email())) {
            User newUser = userMapper.toEntityFromGoogle(userInfo);
            newUser.assignRole(new Role() {{ setId(RoleId.CUSTOMER); }});
            userRepository.saveAndFlush(newUser);
            entityManager.clear();
            log.info("User registered via Google: {}, userId: {}", newUser.getEmail(), newUser.getId());
        }

        User user = userRepository.findWithRoleByEmailIgnoreCase(userInfo.email())
                .orElseThrow(() -> new TokenCreationException("Failed to find user after Google authentication"));

        // If user authenticates via OAuth, account will be activated
        if(!user.getIsActive()) {
            verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.EMAIL_VERIFICATION);
            user.setIsActive(true);
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName())),
                user.getId(),
                user.getRole().getId(),
                user.getUsername()
        );

        Optional<String> accessToken = createAccessToken(userDetails);
        if (accessToken.isEmpty()) {
            throw new TokenCreationException("Failed to create access token");
        }

        String at = createAccessTokenCookie(accessToken.get());
        String refreshToken = tokenUtil.generateToken();
        String rt = createRefreshTokenCookie(refreshToken, true);

        sessionService.createSession(user.getId(), userAgent, refreshToken, true);

        return new LoginResult(
                new AuthResponse(
                        user.getId(),
                        user.getRole().getId(),
                        user.getUsername(),
                        user.getEmail()
                ),
                at,
                rt
        );
    }
}
