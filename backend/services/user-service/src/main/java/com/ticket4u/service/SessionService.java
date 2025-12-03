package com.ticket4u.service;

import com.ticket4u.constant.TokenConstants;
import com.ticket4u.dto.auth.internal.RefreshCreationResult;
import com.ticket4u.entity.Session;
import com.ticket4u.entity.User;
import com.ticket4u.exception.ConcurrentRequestException;
import com.ticket4u.exception.SessionExpiredException;
import com.ticket4u.exception.SessionNotFoundException;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.SessionRepository;
import com.ticket4u.repository.UserRepository;
import com.ticket4u.util.TokenUtil;
import jakarta.persistence.OptimisticLockException;
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
    private final UserMapper userMapper;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    @Transactional
    public void createSession(UUID userId, String userAgent, String sessionToken, Duration ttl) {
        userAgent = userAgent == null? "Unknown" : userAgent;
        if(sessionToken == null || sessionToken.isBlank()) throw new IllegalArgumentException("Session token cannot be null or blank");

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with user Id: " + userId + " not found, skipping session creation"));

        Session session = new Session(user, DigestUtils.sha256Hex(sessionToken) , userAgent, OffsetDateTime.now().plus(ttl));

        sessionRepository.save(session);
    }

    @Transactional
    public void invalidate(String refreshToken) {
        if(refreshToken == null || refreshToken.isBlank()) throw new IllegalArgumentException("Refresh token cannot be null or blank");
        sessionRepository.deleteBySessionHash(DigestUtils.sha256Hex(refreshToken));
    }

    @Transactional
    public RefreshCreationResult refresh(String refreshToken) {
        try {
            Session session = sessionRepository.findBySessionHash(DigestUtils.sha256Hex(refreshToken))
                    .orElseThrow(() -> new SessionNotFoundException("Session not found"));

            if (session.getExpiresAt().isBefore(OffsetDateTime.now())) {
                sessionRepository.delete(session);
                throw new SessionExpiredException("Session has expired");
            }

            String newRefreshToken = tokenUtil.generateToken();
            session.setSessionHash(DigestUtils.sha256Hex(newRefreshToken));

            OffsetDateTime absoluteExp = session.getCreatedAt().plus(TokenConstants.REFRESH_TOKEN.getAbsoluteTTL());
            OffsetDateTime rollingExp = OffsetDateTime.now().plus(TokenConstants.REFRESH_TOKEN.getRollingTTL());
            session.setExpiresAt(absoluteExp.isBefore(rollingExp) ? absoluteExp : rollingExp);

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
}
