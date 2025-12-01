package com.ticket4u.service;

import com.ticket4u.entity.Session;
import com.ticket4u.entity.User;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.repository.SessionRepository;
import com.ticket4u.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createSession(UUID userId, String userAgent, String sessionToken, Duration ttl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with user Id: " + userId + " not found, skipping session creation"));

        Session session = new Session(user, DigestUtils.sha256Hex(sessionToken) , userAgent, OffsetDateTime.now().plus(ttl));

        sessionRepository.save(session);
    }

    @Transactional
    public void invalidate(String sessionToken) {
        sessionRepository.deleteBySessionHash(DigestUtils.sha256Hex(sessionToken));
    }

    @Transactional
    public int deleteExpiredSessions() {
        OffsetDateTime now = OffsetDateTime.now();
        int deleted = sessionRepository.deleteByExpiresAtBefore(now);
        log.info("Deleted {} expired sessions", deleted);
        return deleted;
    }
}
