package com.util;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY; // Must be 256 bits (32 bytes) for HS256

    @Value("${application.security.jwt.expiration-minutes}")
    private long jwtExpirationMinutes;

    private SecretKeySpec getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), JWSAlgorithm.HS256.getName());
    }

    public String generateToken(UserDetails userDetails) throws JOSEException {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(jwtExpirationMinutes));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issuer("your-api-issuer")
                .issueTime(now)
                .expirationTime(expirationDate)
                .jwtID(UUID.randomUUID().toString()) // Recommended unique ID
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .keyID(UUID.randomUUID().toString()) // Optional key ID
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // Sign the JWT
        JWSSigner signer = new MACSigner(getSigningKey());
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    private JWTClaimsSet extractClaims(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Verify the signature
        JWSVerifier verifier = new MACVerifier(getSigningKey());
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature.");
        }

        return signedJWT.getJWTClaimsSet();
    }

    public String extractUsername(String token) throws ParseException, JOSEException {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) throws ParseException, JOSEException {
        final JWTClaimsSet claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws ParseException, JOSEException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws ParseException, JOSEException {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws ParseException, JOSEException {
        return extractClaim(token, JWTClaimsSet::getExpirationTime);
    }
}
