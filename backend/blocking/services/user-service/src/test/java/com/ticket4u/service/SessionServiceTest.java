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
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserService userService;

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
        Boolean persistent = false;

        User mockUser = new User();
        mockUser.setId(userId);

        when(userService.findEntityByIdOrThrow(userId)).thenReturn(mockUser);

        // Act
        sessionService.createSession(userId, userAgent, sessionToken, persistent);

        // Assert
        verify(userService).findEntityByIdOrThrow(userId);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void createSession_ShouldUseUnknownUserAgent_WhenUserAgentIsNull() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String sessionToken = "test-session-token";

        User mockUser = new User();
        when(userService.findEntityByIdOrThrow(userId)).thenReturn(mockUser);

        // Act
        sessionService.createSession(userId, null, sessionToken, false);

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
            sessionService.createSession(userId, "userAgent", null, false);
        });

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void createSession_ShouldThrowException_WhenSessionTokenIsBlank() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createSession(userId, "userAgent", "   ", false);
        });

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void createSession_ShouldCreateShortSession_WhenPersistentFalse() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String userAgent = "Mozilla/5.0";
        String sessionToken = "test-session-token";

        User mockUser = new User();
        mockUser.setId(userId);

        when(userService.findEntityByIdOrThrow(userId)).thenReturn(mockUser);

        // Act
        sessionService.createSession(userId, userAgent, sessionToken, false);

        // Assert
        verify(userService).findEntityByIdOrThrow(userId);
        verify(sessionRepository).save(argThat(session -> {
            // Verify it's a non-persistent session
            assertFalse(session.getPersistent());

            // Verify expiration is approximately 1 day from now
            OffsetDateTime expectedExpiration = OffsetDateTime.now().plusDays(1);
            OffsetDateTime actualExpiration = session.getExpiresAt();

            // Allow 5 second tolerance for test execution time
            long secondsDifference = Math.abs(
                    java.time.Duration.between(expectedExpiration, actualExpiration).getSeconds()
            );
            assertTrue(secondsDifference < 5,
                    "Expiration should be ~1 day from now, but difference was " + secondsDifference + " seconds");

            // Verify other fields
            assertEquals(DigestUtils.sha256Hex(sessionToken), session.getSessionHash());
            assertEquals(userAgent, session.getUserAgent());
            assertEquals(mockUser, session.getUser());

            return true;
        }));
    }

    @Test
    void createSession_ShouldCreateLongSession_WhenPersistentTrue() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String userAgent = "Mozilla/5.0";
        String sessionToken = "test-session-token";

        User mockUser = new User();
        mockUser.setId(userId);

        when(userService.findEntityByIdOrThrow(userId)).thenReturn(mockUser);

        // Act
        sessionService.createSession(userId, userAgent, sessionToken, true);

        // Assert
        verify(sessionRepository).save(argThat(session -> {
            // Verify it's a persistent session
            assertTrue(session.getPersistent());

            // Verify expiration is approximately 7 days from now (rolling TTL)
            OffsetDateTime expectedExpiration = OffsetDateTime.now()
                    .plus(TokenConstants.REFRESH_TOKEN.getRollingTTL());
            OffsetDateTime actualExpiration = session.getExpiresAt();

            // Allow 5 second tolerance
            long secondsDifference = Math.abs(
                    java.time.Duration.between(expectedExpiration, actualExpiration).getSeconds()
            );
            assertTrue(secondsDifference < 5,
                    "Expiration should be ~7 days from now, but difference was " + secondsDifference + " seconds");

            return true;
        }));
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
        mockSession.setPersistent(true);

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
        mockSession.setPersistent(true);

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
