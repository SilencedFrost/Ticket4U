package com.ticket4u.repository;

import com.ticket4u.constant.TokenType;
import com.ticket4u.entity.VerificationToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<VerificationToken> findByTokenHashAndTokenType(String tokenHash, TokenType tokenType);

    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.user.id = :userId AND t.tokenType = :tokenType")
    void deleteAllByUserIdAndTokenType(@Param("userId") UUID userId, @Param("tokenType") TokenType tokenType);

    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(OffsetDateTime now);
}
