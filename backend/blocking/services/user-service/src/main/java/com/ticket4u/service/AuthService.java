package com.ticket4u.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nimbusds.jose.JOSEException;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.*;
import com.ticket4u.dto.auth.internal.LoginResult;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.dto.auth.internal.RefreshResult;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.entity.Role;
import com.ticket4u.entity.User;
import com.ticket4u.exception.EmailAlreadyExistException;
import com.ticket4u.exception.InvalidGoogleTokenException;
import com.ticket4u.exception.PhoneNumberAlreadyExistException;
import com.ticket4u.exception.TokenCreationException;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.util.CookieUtil;
import com.ticket4u.util.JwtUtil;
import com.ticket4u.util.TokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier googleTokenVerifier;

    @PostConstruct
    // Initialize Google ID Token Verifier
    public void init() {
        this.googleTokenVerifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        
        log.info("Google ID Token Verifier initialized with client ID: {}", googleClientId);
    }

    @Transactional
    public LoginResult login(
            @Valid LoginRequest loginRequest,
            String oldRefreshToken,
            String userAgent
    ) {
        // Get CustomUserDetails, will fail here if invalid credentials were provided
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

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

        if(accessToken.isEmpty()) {
            throw new TokenCreationException("Failed to create access token");
        }

        String at = createAccessTokenCookie(accessToken.get());

        String refreshToken = tokenUtil.generateToken();
        String rt = createRefreshTokenCookie(refreshToken, loginRequest.rememberMe());

        // Create session object
        sessionService.createSession(authenticatedUser.getUserId(), userAgent, refreshToken, loginRequest.rememberMe());

        if(oldRefreshToken != null) {
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
    public RefreshResult refresh(String refreshToken) {
        RefreshCreationResult refreshCreationResult = sessionService.refresh(refreshToken);
        UserResponse userResponse = refreshCreationResult.userResponse();

        if (userResponse == null) {
            log.error("Refresh returned null user response");
            sessionService.invalidate(refreshToken);
            return this.createDeleteCookiesResult();
        }

        Optional<String> accessToken = createAccessToken(userResponse);

        if(accessToken.isEmpty()) {
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

    private String createAccessTokenCookie(String accessToken) {
        if(accessToken.isEmpty()) {
            throw new TokenCreationException("Access token is empty");
        }
        // Build access token cookie
        ResponseCookie at = CookieUtil.secureBuilder(TokenConstants.ACCESS_TOKEN.getCookieKey(), accessToken)
                .maxAge(TokenConstants.ACCESS_TOKEN.getAbsoluteTTL())
                .build();
        return at.toString();
    }

    private static String createRefreshTokenCookie(String refreshToken, boolean rememberMe) {
        // normal if remember me, else only one day
        return createRefreshTokenCookie(refreshToken, parseRefreshTokenTTL(rememberMe));
    }

    private static Duration parseRefreshTokenTTL(boolean rememberMe) {
        return rememberMe? TokenConstants.REFRESH_TOKEN.getRollingTTL() : Duration.ofDays(1);
    }

    private static String createRefreshTokenCookie(String refreshToken, Duration ttl) {
        ResponseCookie.ResponseCookieBuilder rtBuilder = CookieUtil.secureBuilder(TokenConstants.REFRESH_TOKEN.getCookieKey(), refreshToken);

        return rtBuilder.maxAge(ttl).build().toString();
    }

    private RefreshResult createDeleteCookiesResult() {
        return new RefreshResult(
                cookieUtil.createDeleteCookie(TokenConstants.ACCESS_TOKEN.getCookieKey()).toString(),
                null,
                cookieUtil.createDeleteCookie(TokenConstants.REFRESH_TOKEN.getCookieKey()).toString()
        );
    }

    /**
     * Register new user with email/password
     * Account will be INACTIVE until email verification
     */
    @Transactional
    public RegisterResponse registerWithEmail(@Valid RegisterRequest request) {
        log.debug("Register request received for email: {}", request.email());

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            log.warn("Registration failed: email already exists - {}", request.email());
            throw new EmailAlreadyExistException("Email already registered: " + request.email());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
                log.warn("Registration failed: phone number already exists - {}", request.phoneNumber());
                throw new PhoneNumberAlreadyExistException("Phone number already registered");
            }
        }

        User user = new User();
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setIsActive(false);
        user.setIsDeleted(false);
        
        String username = request.email().split("@")[0];
        user.setUsername(username);

        if (request.fullName() != null && !request.fullName().isBlank()) {
            String[] names = request.fullName().trim().split("\\s+", 2);
            user.setFirstName(names[0]);
            if (names.length > 1) {
                user.setLastName(names[1]);
            }
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            user.setPhoneNumber(normalizePhoneNumber(request.phoneNumber()));
        }

        Role userRole = new Role();
        userRole.setId(0);
        user.assignRole(userRole);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with email: {}, userId: {}", savedUser.getEmail(), savedUser.getId());

        // TODO: Send verification email
        log.debug("Email verification token should be sent to: {}", savedUser.getEmail());

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Registration successful. Please check your email for verification link."
        );
    }

    /**
     * Register new user with Google OAuth2
     * Email is auto-verified, account is ACTIVE immediately
     */
    @Transactional
    public RegisterResponse registerWithGoogle(@Valid OAuth2RegisterRequest request) {
        log.debug("Google register request received");

        // TODO: Verify Google ID token and extract email
        String email = verifyGoogleTokenAndExtractEmail(request.idToken());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            log.warn("Registration failed: email already exists - {}", email);
            throw new EmailAlreadyExistException("Email already registered: " + email);
        }

        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setIsActive(true);
        user.setIsDeleted(false);

        String username = email.split("@")[0];
        user.setUsername(username);

        if (request.fullName() != null && !request.fullName().isBlank()) {
            String[] names = request.fullName().trim().split("\\s+", 2);
            user.setFirstName(names[0]);
            if (names.length > 1) {
                user.setLastName(names[1]);
            }
        }

        Role userRole = new Role();
        userRole.setId(0);
        user.assignRole(userRole);

        User savedUser = userRepository.save(user);
        log.info("User registered via Google successfully: {}, userId: {}", savedUser.getEmail(), savedUser.getId());

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Registration successful via Google. Your account is ready to use."
        );
    }

    /**
     * Normalize phone number to standard format (remove spaces, convert +84 to 0)
     */
    private String normalizePhoneNumber(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("[\\s-]", "");
        if (cleaned.startsWith("+84")) {
            return "0" + cleaned.substring(3);
        } else if (cleaned.startsWith("84")) {
            return "0" + cleaned.substring(2);
        }
        return cleaned;
    }

    /**
     * Verify Google ID token and extract email
     * Uses Google's official token verification API
     */
    private String verifyGoogleTokenAndExtractEmail(String idToken) {
        try {
            log.debug("Verifying Google ID token");
            
            GoogleIdToken googleIdToken = googleTokenVerifier.verify(idToken);
            
            if (googleIdToken == null) {
                log.warn("Google ID token verification failed - token is invalid");
                throw new InvalidGoogleTokenException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();

            if (email == null || email.isBlank()) {
                log.error("Google token payload does not contain email");
                throw new InvalidGoogleTokenException("Email not found in Google token");
            }

            if (!emailVerified) {
                log.warn("Google email is not verified: {}", email);
                throw new InvalidGoogleTokenException("Google email is not verified");
            }

            log.info("Google token verified successfully for email: {}", email);
            return email;

        } catch (InvalidGoogleTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to verify Google token", e);
            throw new InvalidGoogleTokenException("Failed to verify Google token: " + e.getMessage(), e);
        }
    }
}
