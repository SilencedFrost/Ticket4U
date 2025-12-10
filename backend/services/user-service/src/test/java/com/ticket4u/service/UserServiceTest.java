package com.ticket4u.service;

import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.User;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID testUserId;
    private User testUser;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(testUserId);

        testUserResponse = new UserResponse(
                testUserId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUserResponse() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserResponse);

        // Act
        Optional<UserResponse> result = userService.findById(testUserId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testUserResponse);
        assertThat(result.get().id()).isEqualTo(testUserId);

        verify(userRepository, times(1)).findById(testUserId);
        verify(userMapper, times(1)).toDTO(testUser);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        Optional<UserResponse> result = userService.findById(testUserId);

        // Assert
        assertThat(result).isEmpty();

        verify(userRepository, times(1)).findById(testUserId);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void findByIdOrThrow_WhenUserExists_ShouldReturnUserResponse() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserResponse);

        // Act
        UserResponse result = userService.findByIdOrThrow(testUserId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testUserResponse);
        assertThat(result.id()).isEqualTo(testUserId);

        verify(userRepository, times(1)).findById(testUserId);
        verify(userMapper, times(1)).toDTO(testUser);
    }

    @Test
    void findByIdOrThrow_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findByIdOrThrow(testUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with user Id: " + testUserId + " not found");

        verify(userRepository, times(1)).findById(testUserId);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    void findEntityByIdOrThrow_WhenUserExists_ShouldReturnUserEntity() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findEntityByIdOrThrow(testUserId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testUser);
        assertThat(result.getId()).isEqualTo(testUserId);

        verify(userRepository, times(1)).findById(testUserId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void findEntityByIdOrThrow_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findEntityByIdOrThrow(testUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with user Id: " + testUserId + " not found");

        verify(userRepository, times(1)).findById(testUserId);
        verifyNoInteractions(userMapper);
    }
}