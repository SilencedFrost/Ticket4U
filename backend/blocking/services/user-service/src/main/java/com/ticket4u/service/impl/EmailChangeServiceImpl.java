package com.ticket4u.service.impl;


import com.ticket4u.constant.TokenType;
import com.ticket4u.dto.user.ChangeEmailRequest;
import com.ticket4u.entity.User;
import com.ticket4u.entity.VerificationToken;
import com.ticket4u.exception.InvalidPasswordException;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.service.EmailChangeService;
import com.ticket4u.service.MailServiceClient;
import com.ticket4u.service.VerificationTokenService;
import com.ticket4u.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailChangeServiceImpl implements EmailChangeService {
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final MailServiceClient mailServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void initiateEmailChange(UUID userId, ChangeEmailRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user.error.not_found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("auth.error.invalid.password");
        }

        String normalizedNewEmail = EmailUtil.normalizeEmail(request.newEmail());

        // Tránh enumerable attack: không throw nếu email đã tồn tại, chỉ silent ignore
        if (userRepository.existsByNormalizedEmail(normalizedNewEmail)) {
            return;
        }

        VerificationTokenService.IssuedToken issued = verificationTokenService.issueToken(
                user,
                TokenType.EMAIL_CHANGE,
                "/auth/verify-email-change",
                request.newEmail()
        );

        verificationTokenService.runAfterCommit(() ->
                mailServiceClient.sendEmailChangeVerification(
                        request.newEmail(),
                        user.getUsername(),
                        issued.link(),
                        issued.expiryHours()
                )
        );
    }

    @Override
    public void confirmEmailChange(String token) {
        VerificationToken vt = verificationTokenService.getValidTokenOrThrow(
                token,
                TokenType.EMAIL_CHANGE,
                "auth.error.token.invalid",
                "auth.error.token.expired"
        );

        User user = vt.getUser();
        String newEmail = vt.getPendingEmail();
        String normalizedNewEmail = EmailUtil.normalizeEmail(newEmail);

        // Race condition: email có thể bị đăng ký trong lúc user chờ confirm
        if (userRepository.existsByNormalizedEmail(normalizedNewEmail)) {
            verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.EMAIL_CHANGE);
            throw new InvalidPasswordException("auth.error.email.unavailable");
        }

        user.setEmail(newEmail);
        user.setNormalizedEmail(normalizedNewEmail);
        userRepository.save(user);

        verificationTokenService.deleteTokensByUserAndType(user.getId(), TokenType.EMAIL_CHANGE);
    }
}
