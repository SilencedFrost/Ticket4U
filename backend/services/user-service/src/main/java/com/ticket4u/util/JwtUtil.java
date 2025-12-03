package com.ticket4u.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.ticket4u.constant.TokenConstants;
import com.ticket4u.entity.CustomUserDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.issuer}")
    private String ISSUER;

    @Value("${application.security.jwt.private-key-path}")
    private String privateKeyPath;

    @Value("${application.security.jwt.public-key-path}")
    private String publicKeyPath;

    @Value("${application.security.jwt.key-id}")
    private String keyId;

    private DefaultJWTProcessor<SecurityContext> jwtProcessor;

    private final Cache<String, JWTClaimsSet> claimsCache;

    public JwtUtil() {
        this.claimsCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @PostConstruct
    public void init() throws Exception{
        ECKey publicKey = loadPublicKey();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(
                new JWKSet(publicKey)
        );

        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.ES256,
                jwkSource
        );

        this.jwtProcessor = new DefaultJWTProcessor<>();
        this.jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));
        this.jwtProcessor.setJWSKeySelector(jwsKeySelector);

        this.jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder()
                        .issuer(this.ISSUER)
                        .build(),
                new HashSet<>(List.of("exp", "iat"))
        ));
    }

    private ECKey loadPrivateKey() throws Exception {
        String privateKeyPEM = IOUtils.readInputStreamToString(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(privateKeyPath))
        );
        ECKey parsedKey = ECKey.parseFromPEMEncodedObjects(privateKeyPEM).toECKey();

        return new ECKey.Builder(parsedKey)
                .keyID(keyId)
                .build();
    }

    private ECKey loadPublicKey() throws Exception {
        String publicKeyPEM = IOUtils.readInputStreamToString(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(publicKeyPath))
        );
        ECKey parsedKey = ECKey.parseFromPEMEncodedObjects(publicKeyPEM).toECKey();

        return new ECKey.Builder(parsedKey)
                .keyID(keyId)
                .build();
    }

    public String generateToken(String subject, Map<String, Object> claims, long ttl) throws JOSEException {
        Instant now = Instant.now();
        Instant expiration = now.plus(ttl, ChronoUnit.SECONDS);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(this.ISSUER)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString());

        if (claims != null && !claims.isEmpty()) {
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                claimsBuilder.claim(entry.getKey(), entry.getValue());
            }
        }

        JWTClaimsSet claimsSet = claimsBuilder.build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(this.keyId)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            ECKey privateKey = loadPrivateKey();
            JWSSigner signer = new ECDSASigner(privateKey);
            signedJWT.sign(signer);
        } catch (Exception e) {
            throw new JOSEException("Error signing JWT with EC key", e);
        }

        return signedJWT.serialize();
    }

    public String generateToken(String subject, Map<String, Object> claims, Duration ttl) throws JOSEException {
        return generateToken(subject, claims, ttl.toSeconds());
    }

    public String generateAuthToken(CustomUserDetails userDetails) throws JOSEException {

        UUID userId = userDetails.getUserId();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .toList();

        Map<String, Object> claims = Map.of("roles", roles);

        long ttl = TokenConstants.ACCESS_TOKEN.getAbsoluteTTL().toSeconds();

        return generateToken(userId.toString(), claims, ttl);
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

    public boolean validate(String token) {
        try {
            jwtProcessor.process(token, null);
            return true;
        } catch (Exception e) {
            return false;
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
