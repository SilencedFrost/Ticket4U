package com.ticket4u.service;

import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.entity.User;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.mapper.UserMapper;
import com.ticket4u.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

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

}
