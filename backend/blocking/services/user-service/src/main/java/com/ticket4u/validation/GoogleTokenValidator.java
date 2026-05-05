package com.ticket4u.validation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ticket4u.dto.auth.GoogleUserInfo;
import com.ticket4u.exception.InvalidGoogleTokenException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class GoogleTokenValidator {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier googleTokenVerifier;

    @PostConstruct
    public void init() {
        this.googleTokenVerifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        log.info("Google ID Token Verifier initialized with client ID: {}", googleClientId);
    }

    /**
     * Verify Google ID token and extract user information
     *
     * @param idToken the Google ID token to verify
     * @return GoogleUserInfo containing verified user information
     * @throws InvalidGoogleTokenException if token is invalid or verification fails
     */
    public GoogleUserInfo verifyAndExtract(com.ticket4u.dto.auth.GoogleIdToken idToken) {
        try {
            log.debug("Verifying Google ID token");

            GoogleIdToken googleIdToken = googleTokenVerifier.verify(idToken.value());

            if (googleIdToken == null) {
                log.warn("Google ID token verification failed - token is invalid");
                throw new InvalidGoogleTokenException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            if (email == null || email.isBlank()) {
                log.error("Google token payload does not contain email");
                throw new InvalidGoogleTokenException("Email not found in Google token");
            }

            if (!emailVerified) {
                log.warn("Google email is not verified: {}", email);
                throw new InvalidGoogleTokenException("Google email is not verified");
            }

            log.info("Google token verified successfully for email: {}", email);
            return new GoogleUserInfo(email, name, picture);

        } catch (InvalidGoogleTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to verify Google token", e);
            throw new InvalidGoogleTokenException("Failed to verify Google token: " + e.getMessage(), e);
        }
    }
}
