package com.repository;

import com.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository <Session, UUID> {
    Optional<Session> findBySessionHash(String sessionTokenHash);
    void deleteBySessionHash(String sessionTokenHash);
}
