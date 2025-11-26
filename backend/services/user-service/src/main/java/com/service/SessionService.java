package com.service;

import com.entity.Session;
import com.entity.User;
import com.exception.UserNotFoundException;
import com.mapper.UserMapper;
import com.repository.SessionRepository;
import com.repository.UserRepository;
import com.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final TokenUtil tokenUtil;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void createSession(UUID userId, String userAgent, String sessionToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with user Id: " + userId + " not found, skipping session creation"));

        Session session = new Session(user, DigestUtils.sha256Hex(sessionToken) , userAgent);

        sessionRepository.save(session);
    }

    @Transactional
    public void invalidate(String sessionToken) {
        sessionRepository.deleteBySessionHash(DigestUtils.sha256Hex(sessionToken));
    }
}
