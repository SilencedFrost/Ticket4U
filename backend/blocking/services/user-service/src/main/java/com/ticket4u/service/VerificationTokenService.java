package com.ticket4u.service;

import com.ticket4u.config.VerificationProperties;
import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.User;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.exception.VerificationTokenExpiredException;
import com.ticket4u.exception.VerificationTokenNotFoundException;
import com.ticket4u.repository.VerificationTokenRepository;
import com.ticket4u.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final TokenUtil tokenUtil;
    private final VerificationProperties verificationProperties;
    private final VerificationTokenRepository tokenRepository;

    public IssuedToken issueToken(User user, TokenType tokenType, String frontendPath) {
        deleteTokensByUserAndType(user.getId(), tokenType);

        String plainToken = tokenUtil.generateToken();
        String tokenHash = DigestUtils.sha256Hex(plainToken);

        int expiryHours = resolveExpiryHours(tokenType);
        OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(expiryHours);

        VerificationToken verificationToken = new VerificationToken(tokenHash, tokenType, user, expiresAt);
        tokenRepository.save(verificationToken);

        String link = verificationProperties.getFrontendBaseUrl() + frontendPath + "?token=" + plainToken;
        return new IssuedToken(plainToken, link, expiryHours);
    }

    public VerificationToken getValidTokenOrThrow(
            String plainToken,
            TokenType tokenType,
            String invalidMessageKey,
            String expiredMessageKey
    ) {
        if (plainToken == null || plainToken.length() != TokenUtil.EXPECTED_TOKEN_LENGTH) {
            throw new VerificationTokenNotFoundException(invalidMessageKey);
        }

        String tokenHash = DigestUtils.sha256Hex(plainToken);

        VerificationToken verificationToken = tokenRepository.findByTokenHashAndTokenType(tokenHash, tokenType)
                .orElseThrow(() -> new VerificationTokenNotFoundException(invalidMessageKey));

        if (verificationToken.isExpired()) {
            tokenRepository.delete(verificationToken);
            throw new VerificationTokenExpiredException(expiredMessageKey);
        }

        return verificationToken;
    }

    public void deleteTokensByUserAndType(UUID userId, TokenType tokenType) {
        tokenRepository.deleteAllByUserIdAndTokenType(userId, tokenType);
    }

    public void runAfterCommit(Runnable callback) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                callback.run();
            }
        });
    }

    private int resolveExpiryHours(TokenType tokenType) {
        if (tokenType == TokenType.PASSWORD_RESET) {
            return verificationProperties.getResetTokenExpiryHours();
        }
        return verificationProperties.getTokenExpiryHours();
    }

    public record IssuedToken(String plainToken, String link, int expiryHours) {
    }
}