package com.ticket4u.jwk.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.ticket4u.jwk.exception.JwtProcessingException;
import com.ticket4u.jwk.exception.JwtValidationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final int MAX_CACHE_SIZE = 1000;
    private static final long MIN_CACHE_TTL_SECONDS = 1;

    private final Cache<String, JWTClaimsSet> claimsCache;

    private static final Set<JWSAlgorithm> ALLOWED_ALGORITHMS = Set.of(
            JWSAlgorithm.RS256,
            JWSAlgorithm.RS384,
            JWSAlgorithm.RS512,
            JWSAlgorithm.ES256,
            JWSAlgorithm.ES384,
            JWSAlgorithm.ES512
    );

    public JwtUtil() {
        this.claimsCache = Caffeine.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .build();
    }

    /**
     * Creates a JWT processor configured with the provided public key
     */
    private DefaultJWTProcessor<SecurityContext> createJwtProcessor(JWK publicKey) throws JwtProcessingException {
        try {
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(
                    new JWKSet(publicKey)
            );

            JWSAlgorithm algorithm = JWSAlgorithm.parse(publicKey.getAlgorithm().getName());

            if (!ALLOWED_ALGORITHMS.contains(algorithm)) {
                throw new JwtProcessingException("Unsupported algorithm: " + algorithm);
            }

            return getSecurityContextDefaultJWTProcessor(algorithm, jwkSource);
        } catch (Exception e) {
            logger.error("Failed to create JWT processor: {}", e.getMessage());
            throw new JwtProcessingException("Failed to create JWT processor", e);
        }
    }

    private static DefaultJWTProcessor<SecurityContext> getSecurityContextDefaultJWTProcessor(JWSAlgorithm algorithm, JWKSource<SecurityContext> jwkSource) {
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(
                algorithm,
                jwkSource
        );

        DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
        processor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));
        processor.setJWSKeySelector(jwsKeySelector);

        processor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                null,
                new HashSet<>(List.of("exp", "iat"))
        ));
        return processor;
    }

    /**
     * Extracts and validates claims from a token using the provided public key
     * Caches the claims with TTL based on JWT expiration time
     */
    private JWTClaimsSet extractValidClaims(String token, JWK publicKey) throws JwtValidationException {
        String cacheKey = generateCacheKey(token, publicKey);

        // Check cache first
        JWTClaimsSet cached = claimsCache.getIfPresent(cacheKey);
        if (cached != null) {
            // Verify the cached claims are still valid (not expired)
            if (isClaimsValid(cached)) {
                logger.debug("Returning cached claims for token");
                return cached;
            } else {
                // Remove expired claims from cache
                claimsCache.invalidate(cacheKey);
                logger.debug("Cached claims expired, removed from cache");
            }
        }

        // Process and validate the token
        try {
            DefaultJWTProcessor<SecurityContext> processor = createJwtProcessor(publicKey);
            JWTClaimsSet claims = processor.process(token, null);

            // Cache with TTL based on JWT expiration
            cacheClaimsWithTTL(cacheKey, claims);

            return claims;
        } catch (BadJOSEException e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
            throw new JwtValidationException("JWT validation failed: " + e.getMessage(), e);
        } catch (ParseException e) {
            logger.warn("JWT parsing failed: {}", e.getMessage());
            throw new JwtValidationException("JWT parsing failed: " + e.getMessage(), e);
        } catch (JOSEException e) {
            logger.warn("JWT processing failed: {}", e.getMessage());
            throw new JwtValidationException("JWT processing failed: " + e.getMessage(), e);
        } catch (JwtProcessingException e) {
            logger.error("JWT processor creation failed: {}", e.getMessage());
            throw new JwtValidationException("JWT processor creation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the claims are still valid (not expired)
     */
    private boolean isClaimsValid(JWTClaimsSet claims) {
        Date expirationTime = claims.getExpirationTime();
        if (expirationTime == null) {
            logger.warn("JWT has no expiration time");
            return false;
        }
        return expirationTime.after(new Date());
    }

    /**
     * Caches claims with TTL based on JWT expiration time
     * Note: Caffeine doesn't support per-entry TTL, so we rely on manual validation via isClaimsValid()
     */
    private void cacheClaimsWithTTL(String cacheKey, JWTClaimsSet claims) {
        Date expirationTime = claims.getExpirationTime();

        if (expirationTime != null) {
            long ttlMillis = expirationTime.getTime() - System.currentTimeMillis();
            long ttlSeconds = TimeUnit.MILLISECONDS.toSeconds(ttlMillis);

            // Only cache if TTL is positive and reasonable
            if (ttlSeconds > MIN_CACHE_TTL_SECONDS) {
                claimsCache.put(cacheKey, claims);
                logger.debug("Cached JWT claims with {} seconds until expiration", ttlSeconds);
            } else {
                logger.debug("JWT expires too soon ({}s), not caching", ttlSeconds);
            }
        } else {
            logger.warn("JWT has no expiration time, not caching");
        }
    }

    /**
     * Generates a cache key based on token and public key to avoid collisions
     */
    private String generateCacheKey(String token, JWK publicKey) throws JwtValidationException {
        try {
            String keyId = publicKey.getKeyID() != null
                    ? publicKey.getKeyID()
                    : publicKey.computeThumbprint().toString();

            // Option 1: Use hash
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest((token + "|" + keyId).getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(hash);
            } catch (NoSuchAlgorithmException e) {
                // Fallback to safer delimiter
                return Base64.getEncoder().encodeToString(
                        (token + "\u0000" + keyId).getBytes(StandardCharsets.UTF_8)
                );
            }
        } catch (JOSEException e) {
            logger.error("Failed to generate cache key: {}", e.getMessage());
            throw new JwtValidationException("Failed to generate cache key", e);
        }
    }

    /**
     * Validates a JWT token using the provided public key
     *
     * @param token the JWT token to validate
     * @param publicKey the public key to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean validate(String token, JWK publicKey) {
        try {
            extractValidClaims(token, publicKey);
            logger.debug("JWT validation successful");
            return true;
        } catch (JwtValidationException e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts a specific claim from the token using the provided public key
     *
     * @param token the JWT token
     * @param publicKey the public key to validate against
     * @param claimsResolver function to extract the desired claim
     * @return the extracted claim value
     * @throws JwtValidationException if the token is invalid
     */
    public <T> T extractClaim(String token, JWK publicKey, Function<JWTClaimsSet, T> claimsResolver)
            throws JwtValidationException {
        final JWTClaimsSet claims = extractValidClaims(token, publicKey);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the subject from the token using the provided public key
     *
     * @param token the JWT token
     * @param publicKey the public key to validate against
     * @return the subject claim value
     * @throws JwtValidationException if the token is invalid
     */
    public String extractSubject(String token, JWK publicKey) throws JwtValidationException {
        return extractClaim(token, publicKey, JWTClaimsSet::getSubject);
    }

    /**
     * Clears the claims cache
     */
    public void clearCache() {
        claimsCache.invalidateAll();
        logger.info("JWT claims cache cleared");
    }
}