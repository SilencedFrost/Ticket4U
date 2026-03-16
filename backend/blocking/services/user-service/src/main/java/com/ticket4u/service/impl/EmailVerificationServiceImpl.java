package com.ticket4u.service.impl;

import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.entity.User;
import com.ticket4u.exception.AccountAlreadyActiveException;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.EmailVerificationService;
import com.ticket4u.service.MailServiceClient;
import com.ticket4u.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    private final MailServiceClient mailServiceClient;

    @Override
    @Transactional
    public void sendVerificationEmail(User user) {
        VerificationTokenService.IssuedToken issuedToken = verificationTokenService.issueToken(
                user,
                TokenType.EMAIL_VERIFICATION,
                "/auth/verify-email"
        );

        verificationTokenService.runAfterCommit(() -> {
            try {
                mailServiceClient.sendVerificationEmail(
                        user.getEmail(),
                        user.getUsername(),
                        issuedToken.link(),
                        issuedToken.expiryHours()
                );
                log.info("Verification email queued for user: {}", user.getEmail());
            } catch (Exception ex) {
                log.error("Failed to send verification email for user: {}", user.getEmail(), ex);
            }
        });
    }

    @Override
    @Transactional
    public void verifyToken(String plainToken) {
        VerificationToken verificationToken = verificationTokenService.getValidTokenOrThrow(
                plainToken,
                TokenType.EMAIL_VERIFICATION,
                "auth.verification.token_invalid",
                "auth.verification.token_expired"
        );

        User user = verificationToken.getUser();

        if (Boolean.TRUE.equals(user.getIsActive())) {
            verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.EMAIL_VERIFICATION);
            throw new AccountAlreadyActiveException("auth.verification.already_active");
        }

        user.setIsActive(true);
        userRepository.save(user);
        verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.EMAIL_VERIFICATION);

        log.info("Email verified and account activated for user: {}", user.getEmail());
    }

    @Override
    @Transactional
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
