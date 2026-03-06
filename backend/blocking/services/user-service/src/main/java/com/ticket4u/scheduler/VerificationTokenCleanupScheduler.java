package com.ticket4u.scheduler;

import com.ticket4u.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationTokenCleanupScheduler {

    private final EmailVerificationTokenRepository tokenRepository;

    @Scheduled(fixedDelay = 86400000, initialDelay = 10000)
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting scheduled verification token cleanup");
        int deleted = tokenRepository.deleteExpiredTokens(OffsetDateTime.now());
        log.info("Completed verification token cleanup: {} tokens deleted", deleted);
    }
}
