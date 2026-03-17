package com.ticket4u.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.issuer}")
    private String issuer;

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
    public void init() throws Exception {
        ECKey publicKey = loadPublicKey();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(publicKey));

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.ES256, jwkSource
        );

        this.jwtProcessor = new DefaultJWTProcessor<>();
        this.jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));
        this.jwtProcessor.setJWSKeySelector(keySelector);
        this.jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder().issuer(this.issuer).build(),
                new HashSet<>(List.of("exp", "iat"))
        ));
    }

    private ECKey loadPublicKey() throws Exception {
        String pem = IOUtils.readInputStreamToString(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(publicKeyPath))
        );
        ECKey parsed = ECKey.parseFromPEMEncodedObjects(pem).toECKey();
        Curve curve  = parsed.getCurve();

        JWSAlgorithm algorithm;
        if      (Curve.P_256.equals(curve)) algorithm = JWSAlgorithm.ES256;
        else if (Curve.P_384.equals(curve)) algorithm = JWSAlgorithm.ES384;
        else if (Curve.P_521.equals(curve)) algorithm = JWSAlgorithm.ES512;
        else throw new IllegalArgumentException("Unsupported curve: " + curve);

        return new ECKey.Builder(parsed).keyID(keyId).algorithm(algorithm).build();
    }

    public boolean validate(String token) {
        try {
            jwtProcessor.process(token, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> resolver) {
        JWTClaimsSet cached = claimsCache.getIfPresent(token);
        if (cached != null) return resolver.apply(cached);

        try {
            JWTClaimsSet claims = jwtProcessor.process(token, null);
            claimsCache.put(token, claims);
            return resolver.apply(claims);
        } catch (BadJOSEException e) {
            throw new RuntimeException("JWT validation failed: " + e.getMessage(), e);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("JWT parsing failed: " + e.getMessage(), e);
        }
    }

    public String extractSubject(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }
}