package com.ticket4u.feature.eventdetail.repository;

import com.ticket4u.core.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventDetailRepository extends JpaRepository<Event, UUID> {
    @EntityGraph(attributePaths = {"category", "zones"})
    Optional<Event> findWithDetailsById(UUID id);
}
