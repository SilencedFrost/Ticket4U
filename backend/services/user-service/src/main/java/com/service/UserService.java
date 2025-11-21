package com.service;

import com.dto.auth.LoginRequest;
import com.dto.auth.RegisterRequest;
import com.dto.user.UserCreateRequest;
import com.dto.user.UserResponse;
import com.dto.user.UserUpdateRequest;
import com.entity.Role;
import com.entity.User;
import com.entity.VerificationToken;
import com.exception.RoleNotFoundException;
import com.exception.UserAlreadyExistException;
import com.exception.UserNotFoundException;
import com.mapper.UserMapper;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.repository.VerificationTokenRepository;
import com.util.TokenGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final TokenGeneratorUtil tokenGeneratorUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final HashService hashService;

    private final EmailService emailService;
    private final VerificationTokenRepository tokenRepository;

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    public List<UserResponse> findAll() {
        return findAll(PageRequest.of(0, 50)).getContent();
    }

    public Optional<UserResponse> findById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDTO);
    }

    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).map(userMapper::toDTO);
    }

    public Optional<UserResponse> authenticate(LoginRequest loginRequest) {
        return userRepository.findByEmailIgnoreCase(loginRequest.email())
                .filter(user -> hashService.verifyPassword(loginRequest.password(), user.getPasswordHash()))
                .map(user -> {
                    if (!user.getIsActive()) {
                        log.warn("Login attempt for inactive account: {}", user.getEmail());
                        throw new IllegalStateException("Account is not activated. Please check your email for the verification link.");
                    }
                    return userMapper.toDTO(user);
                });
    }

    @Transactional
    public UserResponse create(UserCreateRequest userCreateRequest) {
        Role role = roleRepository.findById(userCreateRequest.roleId()).orElseThrow(() -> new RoleNotFoundException("Role not found: " + userCreateRequest.roleId()));

        User user = userMapper.toEntity(userCreateRequest, hashService);
        user.assignRole(role);
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public void registerIfNotExist(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.email())) {
            log.info("User with email {} already exists. Skipping creation.", registerRequest.email());
            throw new UserAlreadyExistException("An account with this email already exists.");
        }

        User user = userMapper.toEntity(registerRequest, hashService);
        Role customerRole = roleRepository.findByRoleName("customer")
                .orElseThrow(() -> new RoleNotFoundException("'customer' role not found"));
        user.assignRole(customerRole);

        user.setIsActive(false);

        user = userRepository.save(user);

        String token = tokenGeneratorUtil.generateToken();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setTokenHash(hashService.hashOpaqueKey(token));
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user, token);
    }

    @Transactional
    public boolean verifyToken(String token) {
        Optional<VerificationToken> optToken = tokenRepository.findByTokenHash(hashService.hashOpaqueKey(token));

        if (optToken.isEmpty()) {
            log.warn("Invalid verification token received.");
            return false;
        }

        VerificationToken verificationToken = optToken.get();

        User user = verificationToken.getUser();
        user.setIsActive(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
        log.info("Account activated successfully for user: {}", user.getEmail());

        return true;
    }

    @Transactional
    public UserResponse update(UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(userUpdateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userUpdateRequest.userId()));

        userMapper.updateUserFromDTO(userUpdateRequest, existingUser, hashService);

        return userMapper.toDTO(existingUser);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean updatePasswordByEmail(String email, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        user.setPasswordHash(hashService.hashPassword(newPassword));
        userRepository.save(user);
        return true;
    }
}
