package com.ticket4u.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    private static final int DEFAULT_TOKEN_BYTES = 32;
    public static final int EXPECTED_TOKEN_LENGTH = 43;

    public String generateToken() {
        return generateToken(DEFAULT_TOKEN_BYTES);
    }

    public String generateToken(int tokenBytes) {
        byte[] randomBytes = new byte[tokenBytes];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
