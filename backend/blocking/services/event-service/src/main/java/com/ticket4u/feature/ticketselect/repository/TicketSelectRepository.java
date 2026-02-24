package com.ticket4u.feature.ticketselect.repository;

import com.ticket4u.core.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketSelectRepository extends JpaRepository<Event, UUID> {

    // Eagerly fetch zones so we don't hit N+1 — same pattern as EventDetailRepository
    @EntityGraph(attributePaths = {"zones"})
    Optional<Event> findWithZonesById(UUID id);
}