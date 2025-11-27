package com.ticket4u.scheduler;

import com.ticket4u.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {

    private final SessionService sessionService;

    @Scheduled(fixedDelay = 86400000, initialDelay = 5000)
    public void cleanupExpiredSessions() {
        log.info("Starting scheduled session cleanup");
        int deleted = sessionService.deleteExpiredSessions();
        log.info("Completed session cleanup: {} sessions deleted", deleted);
    }
}
