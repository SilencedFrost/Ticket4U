package com.ticket4u.organizer.repository;

import com.ticket4u.core.entity.EventSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizerSessionRepository extends JpaRepository<EventSession, UUID> {

    List<EventSession> findAllByEventIdOrderByStartDateAsc(UUID eventId);

    @Query("""
        SELECT s FROM EventSession s
        WHERE s.id = :sessionId
        AND s.event.id = :eventId
        AND s.event.organizerId = :organizerId
    """)
    Optional<EventSession> findByIdAndEventIdAndOrganizerId(
            @Param("sessionId")   UUID sessionId,
            @Param("eventId")     UUID eventId,
            @Param("organizerId") UUID organizerId
    );

    @Query("""
        SELECT s FROM EventSession s
        WHERE s.id = :sessionId
        AND s.event.organizerId = :organizerId
    """)
    Optional<EventSession> findByIdAndOrganizerId(
            @Param("sessionId")   UUID sessionId,
            @Param("organizerId") UUID organizerId
    );
}