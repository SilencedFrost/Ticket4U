package com.ticket4u.scheduler;

import com.ticket4u.repository.UserRepository;
import com.ticket4u.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class InactiveAccountCleanupScheduler {

    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 3600000, initialDelay = 15000)
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting inactive account cleanup");
        int deleted = userRepository.deleteInactiveAccounts(OffsetDateTime.now().minusDays(7));
        log.info("Completed inactive account cleanup: {} accounts deleted", deleted);
    }
}
