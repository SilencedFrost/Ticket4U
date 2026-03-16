package com.ticket4u.service.impl;

import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.entity.User;
import com.ticket4u.repository.SessionRepository;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.MailServiceClient;
import com.ticket4u.service.PasswordResetService;
import com.ticket4u.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final VerificationTokenService verificationTokenService;
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
        VerificationTokenService.IssuedToken issuedToken = verificationTokenService.issueToken(
                user,
                TokenType.PASSWORD_RESET,
                "/auth/reset-password"
        );

        verificationTokenService.runAfterCommit(() -> {
            try {
                mailServiceClient.sendPasswordResetEmail(
                        user.getEmail(),
                        user.getUsername(),
                        issuedToken.link(),
                        issuedToken.expiryHours()
                );
                log.info("Password reset email queued for user: {}", user.getEmail());
            } catch (Exception ex) {
                log.error("Failed to send password reset email for user: {}", user.getEmail(), ex);
            }
        });
    }

    @Override
    @Transactional
    public void resetPassword(String plainToken, String newPassword) {
        VerificationToken resetToken = verificationTokenService.getValidTokenOrThrow(
                plainToken,
                TokenType.PASSWORD_RESET,
                "auth.password_reset.token_invalid",
                "auth.password_reset.token_expired"
        );

        User user = resetToken.getUser();

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.PASSWORD_RESET);
        sessionRepository.deleteAllByUserId(user.getId());

        log.info("Password reset and all sessions invalidated for user: {}", user.getEmail());
    }
}
