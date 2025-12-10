package com.ticket4u.service;

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
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenUtil tokenUtil;

    @InjectMocks
    private SessionService sessionService;

    // Create session tests

    @Test
    void createSession_ShouldSaveSession_WhenValidInput() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String userAgent = "Mozilla/5.0";
        String sessionToken = "test-session-token";
        Duration ttl = Duration.ofDays(7);

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        sessionService.createSession(userId, userAgent, sessionToken, ttl);

        // Assert
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void createSession_ShouldUseUnknownUserAgent_WhenUserAgentIsNull() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String sessionToken = "test-session-token";
        Duration ttl = Duration.ofDays(7);

        User mockUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        sessionService.createSession(userId, null, sessionToken, ttl);

        // Assert
        verify(sessionRepository).save(argThat(session ->
                session.getUserAgent().equals("Unknown")
        ));
    }

    @Test
    void createSession_ShouldThrowException_WhenSessionTokenIsNull() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createSession(userId, "userAgent", null, Duration.ofDays(7));
        });

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void createSession_ShouldThrowException_WhenSessionTokenIsBlank() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createSession(userId, "userAgent", "   ", Duration.ofDays(7));
        });

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void createSession_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            sessionService.createSession(userId, "userAgent", "token", Duration.ofDays(7));
        });

        verify(sessionRepository, never()).save(any());
    }

    // Invalidate tests

    @Test
    void invalidate_ShouldDeleteSession_WhenValidToken() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        String expectedHash = DigestUtils.sha256Hex(refreshToken);

        // Act
        sessionService.invalidate(refreshToken);

        // Assert
        verify(sessionRepository).deleteBySessionHash(expectedHash);
    }

    @Test
    void invalidate_ShouldThrowException_WhenTokenIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.invalidate(null);
        });

        verify(sessionRepository, never()).deleteBySessionHash(any());
    }

    @Test
    void invalidate_ShouldThrowException_WhenTokenIsBlank() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.invalidate("  ");
        });

        verify(sessionRepository, never()).deleteBySessionHash(any());
    }

    // Refresh tests

    @Test
    void refresh_ShouldReturnNewToken_WhenSessionIsValid() {
        // Arrange
        String oldRefreshToken = "old-token";
        String newRefreshToken = "new-token";
        String oldHash = DigestUtils.sha256Hex(oldRefreshToken);

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());

        Session mockSession = new Session();
        mockSession.setUser(mockUser);
        mockSession.setSessionHash(oldHash);
        mockSession.setCreatedAt(OffsetDateTime.now().minusDays(1));
        mockSession.setExpiresAt(OffsetDateTime.now().plusDays(6));

        when(sessionRepository.findBySessionHash(oldHash)).thenReturn(Optional.of(mockSession));
        when(tokenUtil.generateToken()).thenReturn(newRefreshToken);

        // Act
        RefreshCreationResult result = sessionService.refresh(oldRefreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(newRefreshToken, result.refreshToken());
        verify(sessionRepository).save(mockSession);
        verify(userMapper).toDTO(mockUser);
    }

    @Test
    void refresh_ShouldThrowException_WhenSessionNotFound() {
        // Arrange
        String refreshToken = "non-existent-token";
        String hash = DigestUtils.sha256Hex(refreshToken);

        when(sessionRepository.findBySessionHash(hash)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SessionNotFoundException.class, () -> {
            sessionService.refresh(refreshToken);
        });

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void refresh_ShouldThrowException_WhenSessionIsExpired() {
        // Arrange
        String refreshToken = "expired-token";
        String hash = DigestUtils.sha256Hex(refreshToken);

        Session expiredSession = new Session();
        expiredSession.setExpiresAt(OffsetDateTime.now().minusDays(1));

        when(sessionRepository.findBySessionHash(hash)).thenReturn(Optional.of(expiredSession));

        // Act & Assert
        assertThrows(SessionExpiredException.class, () -> {
            sessionService.refresh(refreshToken);
        });

        verify(sessionRepository).delete(expiredSession);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void refresh_ShouldThrowException_WhenOptimisticLockOccurs() {
        // Arrange
        String refreshToken = "token";
        String hash = DigestUtils.sha256Hex(refreshToken);

        Session mockSession = new Session();
        mockSession.setCreatedAt(OffsetDateTime.now().minusDays(1));
        mockSession.setExpiresAt(OffsetDateTime.now().plusDays(6));

        when(sessionRepository.findBySessionHash(hash)).thenReturn(Optional.of(mockSession));
        when(tokenUtil.generateToken()).thenReturn("new-token");
        when(sessionRepository.save(any())).thenThrow(new OptimisticLockException());

        // Act & Assert
        assertThrows(ConcurrentRequestException.class, () -> {
            sessionService.refresh(refreshToken);
        });
    }

    // DeleteExpiredSessions tests

    @Test
    void deleteExpiredSessions_ShouldReturnDeletedCount() {
        // Arrange
        int expectedDeleted = 5;
        when(sessionRepository.deleteByExpiresAtBefore(any(OffsetDateTime.class)))
                .thenReturn(expectedDeleted);

        // Act
        int result = sessionService.deleteExpiredSessions();

        // Assert
        assertEquals(expectedDeleted, result);
        verify(sessionRepository).deleteByExpiresAtBefore(any(OffsetDateTime.class));
    }

    @Test
    void deleteExpiredSessions_ShouldReturnZero_WhenNoExpiredSessions() {
        // Arrange
        when(sessionRepository.deleteByExpiresAtBefore(any(OffsetDateTime.class)))
                .thenReturn(0);

        // Act
        int result = sessionService.deleteExpiredSessions();

        // Assert
        assertEquals(0, result);
    }
}
