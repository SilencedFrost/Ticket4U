package com.ticket4u.repository;

import com.ticket4u.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    void deleteAllByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(OffsetDateTime now);
}
