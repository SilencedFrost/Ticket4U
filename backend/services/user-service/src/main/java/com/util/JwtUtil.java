package com.util;

import com.entity.CustomUserDetails;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${application.security.jwt.issuer}")
    private String ISSUER;

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.expiration-minutes}")
    private long jwtExpirationMinutes;

    private DefaultJWTProcessor<SecurityContext> jwtProcessor;

    private final Cache<String, JWTClaimsSet> claimsCache;

    public JwtUtil() {
        this.claimsCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @PostConstruct
    public void init() {
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(getSigningKey());

        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.HS256,
                jwkSource
        );

        this.jwtProcessor = new DefaultJWTProcessor<>();

        this.jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));

        this.jwtProcessor.setJWSKeySelector(jwsKeySelector);

        this.jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder()
                        .issuer(ISSUER)
                        .build(),
                new HashSet<>(List.of("exp", "iat"))
        ));
    }

    private SecretKeySpec getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), JWSAlgorithm.HS256.getName());
    }

    public JwtUtil setExpiration(long minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Expiration time must be positive.");
        }
        this.jwtExpirationMinutes = minutes;
        return this;
    }

    public String generateToken(String subject, Map<String, Object> claims) throws JOSEException {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(ISSUER)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString());

        if (claims != null && !claims.isEmpty()) {
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                claimsBuilder.claim(entry.getKey(), entry.getValue());
            }
        }

        JWTClaimsSet claimsSet = claimsBuilder.build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .keyID(UUID.randomUUID().toString())
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        JWSSigner signer = new MACSigner(getSigningKey());
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String generateAuthToken(CustomUserDetails userDetails) throws JOSEException {
        UUID userId = userDetails.getUserId();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .toList();
        return generateToken(userId.toString(), Map.of("roles", roles));
    }

    private JWTClaimsSet extractValidClaims(String token) {
        JWTClaimsSet cached = claimsCache.getIfPresent(token);
        if (cached != null) {
            return cached;
        }

        try {
            JWTClaimsSet claims = jwtProcessor.process(token, null);
            claimsCache.put(token, claims);
            return claims;
        } catch (BadJOSEException e) {
            throw new RuntimeException("JWT validation failed: " + e.getMessage(), e);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("JWT parsing failed: " + e.getMessage(), e);
        }
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        final JWTClaimsSet claims = extractValidClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractSubject(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }
}
