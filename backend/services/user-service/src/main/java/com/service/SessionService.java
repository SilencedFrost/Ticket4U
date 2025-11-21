package com.service;

import com.dto.seller.SellerResponse;
import com.dto.session.SessionCreateRequest;
import com.dto.user.UserResponse;
import com.entity.Session;
import com.entity.User;
import com.exception.SessionNotFoundException;
import com.exception.UserNotFoundException;
import com.mapper.SellerMapper;
import com.mapper.SessionMapper;
import com.mapper.UserMapper;
import com.repository.SellerRepository;
import com.repository.SessionRepository;
import com.repository.UserRepository;
import com.util.TokenGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final TokenGeneratorUtil tokenGeneratorUtil;
    private final SessionRepository sessionRepository;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;
    private final SellerMapper sellerMapper;
    private final HashService hashService;
    private final UserMapper userMapper;

    @Transactional
    public String createSession(long userId, String userAgent) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with user Id: " + userId + " not found, skipping session creation"));

        String sessionToken = tokenGeneratorUtil.generateToken();

        Session session = sessionMapper.toEntity(new SessionCreateRequest(userId, sessionToken, userAgent));
        session.assignUser(user);

        sessionRepository.save(session);

        return sessionToken;
    }

    @Transactional
    public void invalidate(String sessionToken) {
        Session session = sessionRepository.findBySessionHash(hashService.hashOpaqueKey(sessionToken)).orElseThrow(() -> new SessionNotFoundException("Session with session token " + sessionToken + " not found, aborting session invalidation"));

        if(!session.getIsActive()) return;

        session.setLastAccessed(OffsetDateTime.now());
        session.setRevokedAt(OffsetDateTime.now());
        session.setRevokeReason("logout");
        session.setIsActive(false);

        sessionRepository.save(session);
    }

    public Optional<UserResponse> findUserBySessionToken(String sessionToken) {
        return sessionRepository.findBySessionHash(hashService.hashOpaqueKey(sessionToken))
                .filter(Session::getIsActive)
                .map(Session::getUser)
                .map(userMapper::toDTO);
    }

    public Optional<SellerResponse> findSellerBySessionToken(String sessionToken) {
        return sessionRepository.findBySessionHash(hashService.hashOpaqueKey(sessionToken))
                .filter(Session::getIsActive)
                .map(Session::getUser)
                .map(User::getUserId)
                .flatMap(sellerRepository::findByUserUserId)
                .map(sellerMapper::toDTO);
    }
}
