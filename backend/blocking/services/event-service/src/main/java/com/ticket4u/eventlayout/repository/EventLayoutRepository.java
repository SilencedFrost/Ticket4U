package com.ticket4u.eventlayout.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EventLayoutRepository extends JpaRepository<Event, UUID> {

    @EntityGraph(attributePaths = { "sessions", "sessions.zones", "venue" })
    Optional<Event> findWithSessionsZonesAndVenueById(@Param("eventId") UUID eventId);
}