package com.ticket4u.service.impl;

import com.ticket4u.config.VerificationProperties;
import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.entity.User;
import com.ticket4u.exception.VerificationTokenExpiredException;
import com.ticket4u.exception.VerificationTokenNotFoundException;
import com.ticket4u.repository.VerificationTokenRepository;
import com.ticket4u.repository.SessionRepository;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.MailServiceClient;
import com.ticket4u.service.PasswordResetService;
import com.ticket4u.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final TokenUtil tokenUtil;
    private final VerificationProperties verificationProperties;
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final MailServiceClient mailServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendPasswordResetEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

        if (optionalUser.isEmpty() || Boolean.FALSE.equals(optionalUser.get().getIsActive())) {
            log.debug("Password reset ignored for email: {}", email);
            return;
        }

        User user = optionalUser.get();
        tokenRepository.deleteAllByUserIdAndTokenType(user.getId(), TokenType.PASSWORD_RESET);

        String plainToken = tokenUtil.generateToken();
        String tokenHash = DigestUtils.sha256Hex(plainToken);

        int expiryHours = verificationProperties.getResetTokenExpiryHours();
        OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(expiryHours);

        VerificationToken resetToken = new VerificationToken(tokenHash, TokenType.PASSWORD_RESET, user, expiresAt);
        tokenRepository.save(resetToken);

        String resetLink = verificationProperties.getFrontendBaseUrl()
                + "/auth/reset-password?token=" + plainToken;

        sendMailAfterCommit(user.getEmail(), user.getUsername(), resetLink, expiryHours);
    }

    private void sendMailAfterCommit(String email, String username, String resetLink, int expiryHours) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                mailServiceClient.sendPasswordResetEmail(email, username, resetLink, expiryHours);
                log.info("Password reset email queued for user: {}", email);
            }
        });
    }

    @Override
    @Transactional
    public void resetPassword(String plainToken, String newPassword) {
        if (plainToken == null || plainToken.length() != TokenUtil.EXPECTED_TOKEN_LENGTH) {
            throw new VerificationTokenNotFoundException("auth.password_reset.token_invalid");
        }

        String tokenHash = DigestUtils.sha256Hex(plainToken);

        VerificationToken resetToken = tokenRepository.findByTokenHashAndTokenType(tokenHash, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new VerificationTokenNotFoundException("auth.password_reset.token_invalid"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new VerificationTokenExpiredException("auth.password_reset.token_expired");
        }

        User user = resetToken.getUser();

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteAllByUserIdAndTokenType(user.getId(), TokenType.PASSWORD_RESET);
        sessionRepository.deleteAllByUserId(user.getId());

        log.info("Password reset and all sessions invalidated for user: {}", user.getEmail());
    }
}
