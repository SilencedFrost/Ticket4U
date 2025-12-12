package com.ticket4u.service;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.entity.Session;
import com.ticket4u.entity.User;
import com.ticket4u.exception.ConcurrentRequestException;
import com.ticket4u.exception.SessionExpiredException;
import com.ticket4u.exception.SessionNotFoundException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.SessionRepository;
import com.ticket4u.util.TokenUtil;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserService userService;
    private final TokenUtil tokenUtil;
    private final UserMapper userMapper;
    private final SessionRepository sessionRepository;


    @Transactional
    public void createSession(UUID userId, String userAgent, String sessionToken, Boolean persistent) {
        // Validation
        String normalizedUserAgent = normalizeUserAgent(userAgent);
        validateSessionToken(sessionToken);

        // Getting user
        User user = userService.findEntityByIdOrThrow(userId);

        // Session creation
        Session session = new Session(user, DigestUtils.sha256Hex(sessionToken) , normalizedUserAgent, persistent);
        sessionRepository.save(session);
    }

    @Transactional
    public void invalidate(String refreshToken) {
        // Validation
        validateSessionToken(refreshToken);

        // Session invalidation
        sessionRepository.deleteBySessionHash(DigestUtils.sha256Hex(refreshToken));
    }

    @Transactional
    public RefreshCreationResult refresh(String refreshToken) {
        // Validation
        validateSessionToken(refreshToken);

        // Session refresh
        try {
            Session session = sessionRepository.findBySessionHash(DigestUtils.sha256Hex(refreshToken))
                    .orElseThrow(() -> new SessionNotFoundException("Session not found"));

            if (session.getExpiresAt().isBefore(OffsetDateTime.now())) {
                sessionRepository.delete(session);
                throw new SessionExpiredException("Session has expired");
            }

            String newRefreshToken = tokenUtil.generateToken();

            session.setSessionHash(DigestUtils.sha256Hex(newRefreshToken));
            session.setExpiresAt(calculateNewExpiration(session));

            sessionRepository.save(session);

            return new RefreshCreationResult(userMapper.toDTO(session.getUser()), newRefreshToken);
        } catch (OptimisticLockException e) {
            throw new ConcurrentRequestException("Concurrent session refresh detected, denying request");
        }
    }

    @Transactional
    public int deleteExpiredSessions() {
        OffsetDateTime now = OffsetDateTime.now();
        int deleted = sessionRepository.deleteByExpiresAtBefore(now);
        log.info("Deleted {} expired sessions", deleted);
        return deleted;
    }

    private String normalizeUserAgent(String userAgent) {
        return userAgent == null ? "Unknown" : userAgent;
    }

    private void validateSessionToken(String sessionToken) {
        if(sessionToken == null || sessionToken.isBlank()) {
            throw new IllegalArgumentException("Session token cannot be null or blank");
        }
    }

    private OffsetDateTime calculateNewExpiration(Session session) {
        if(session.getPersistent()) {
            OffsetDateTime absoluteExp = session.getCreatedAt()
                    .plus(TokenConstants.REFRESH_TOKEN.getAbsoluteTTL());
            OffsetDateTime rollingExp = OffsetDateTime.now()
                    .plus(TokenConstants.REFRESH_TOKEN.getRollingTTL());

            // Use the earlier of the two
            return absoluteExp.isBefore(rollingExp) ? absoluteExp : rollingExp;
        } else {
            return session.getExpiresAt();
        }
    }
}
