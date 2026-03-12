package com.ticket4u.service.impl;

import com.ticket4u.config.VerificationProperties;
import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.entity.User;
import com.ticket4u.exception.AccountAlreadyActiveException;
import com.ticket4u.exception.VerificationTokenExpiredException;
import com.ticket4u.exception.VerificationTokenNotFoundException;
import com.ticket4u.repository.VerificationTokenRepository;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.EmailVerificationService;
import com.ticket4u.service.MailServiceClient;
import com.ticket4u.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final TokenUtil tokenUtil;
    private final VerificationProperties verificationProperties;
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final MailServiceClient mailServiceClient;

    @Override
    @Transactional
    public void sendVerificationEmail(User user) {
        tokenRepository.deleteAllByUserIdAndTokenType(user.getId(), TokenType.EMAIL_VERIFICATION);

        String plainToken = tokenUtil.generateToken();
        String tokenHash = DigestUtils.sha256Hex(plainToken);

        int expiryHours = verificationProperties.getTokenExpiryHours();
        OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(expiryHours);

        VerificationToken verificationToken = new VerificationToken(tokenHash, TokenType.EMAIL_VERIFICATION, user, expiresAt);
        tokenRepository.save(verificationToken);

        String verificationLink = verificationProperties.getFrontendBaseUrl()
                + "/auth/verify-email?token=" + plainToken;

        sendMailAfterCommit(user.getEmail(), user.getUsername(), verificationLink, expiryHours);
    }

    private void sendMailAfterCommit(String email, String username, String verificationLink, int expiryHours) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                mailServiceClient.sendVerificationEmail(email, username, verificationLink, expiryHours);
                log.info("Verification email queued for user: {}", email);
            }
        });
    }

    @Override
    @Transactional
    public void verifyToken(String plainToken) {
        if (plainToken == null || plainToken.length() != TokenUtil.EXPECTED_TOKEN_LENGTH) {
            throw new VerificationTokenNotFoundException("auth.verification.token_invalid");
        }

        String tokenHash = DigestUtils.sha256Hex(plainToken);

        VerificationToken verificationToken = tokenRepository.findByTokenHashAndTokenType(tokenHash, TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new VerificationTokenNotFoundException("auth.verification.token_invalid"));

        if (verificationToken.isExpired()) {
            tokenRepository.delete(verificationToken);
            throw new VerificationTokenExpiredException("auth.verification.token_expired");
        }

        User user = verificationToken.getUser();

        if (Boolean.TRUE.equals(user.getIsActive())) {
            tokenRepository.delete(verificationToken);
            throw new AccountAlreadyActiveException("auth.verification.already_active");
        }

        user.setIsActive(true);
        userRepository.save(user);
        tokenRepository.deleteAllByUserIdAndTokenType(user.getId(), TokenType.EMAIL_VERIFICATION);

        log.info("Email verified and account activated for user: {}", user.getEmail());
    }

    @Override
    public void resendVerification(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

        if (optionalUser.isEmpty() || Boolean.TRUE.equals(optionalUser.get().getIsActive())) {
            log.debug("Resend verification ignored for email: {}", email);
            return;
        }

        sendVerificationEmail(optionalUser.get());
        log.info("Resent verification email to: {}", email);
    }
}
