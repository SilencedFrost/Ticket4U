package com.ticket4u.service;

import com.ticket4u.dto.user.ChangeInfoRequest;
import com.ticket4u.dto.user.ChangePasswordRequest;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.dto.user.UserSummaryResponse;
import com.ticket4u.entity.User;
import com.ticket4u.exception.InvalidPasswordException;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Read

    public Optional<UserResponse> findById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public UserResponse findByIdOrThrow(UUID id) {
        return this.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with user Id: " + id + " not found")
        );
    }

    public User findEntityByIdOrThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with user Id: " + id + " not found")
        );
    }

    public UserSummaryResponse getSummaryByIdOrThrow(UUID id) {
        User user = this.findEntityByIdOrThrow(id);
        return userMapper.toSummaryResponse(user);
    }

    //Write

    @Transactional
    public UserSummaryResponse updateInfo(UUID userId, ChangeInfoRequest request) {
        User user = findEntityByIdOrThrow(userId);
        userMapper.updateFromChangeInfo(request, user);
        return userMapper.toSummaryResponse(user);
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = findEntityByIdOrThrow(userId);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())){
            throw new InvalidPasswordException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
    }
}
