package com.ticket4u.repository;

import com.ticket4u.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository <Session, UUID> {
    Optional<Session> findBySessionHash(String sessionTokenHash);
    void deleteBySessionHash(String sessionTokenHash);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.user.id = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);

    List<Session> findAllByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :expirationTime")
    int deleteByExpiresAtBefore(@Param("expirationTime") OffsetDateTime expirationTime);
}
