package com.ticket4u.event.layout.repository;

import com.ticket4u.core.entity.EventSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<EventSession, UUID> {

    @Override
    @EntityGraph(value = "EventSession.withAllEntities")
    Optional<EventSession> findById(UUID id);
}