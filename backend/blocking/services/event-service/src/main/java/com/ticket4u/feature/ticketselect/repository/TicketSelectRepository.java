package com.ticket4u.feature.ticketselect.repository;

import com.ticket4u.core.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TicketSelectRepository extends JpaRepository<Event, UUID> {

    // Fetch zones eagerly — avoids N+1 for zone list
    @EntityGraph(attributePaths = {"zones"})
    Optional<Event> findWithZonesById(UUID id);

    // Fetch event layouts + venue layout JSON + modifications in one query
    @Query("""
        SELECT DISTINCT e FROM Event e
        LEFT JOIN FETCH e.eventLayouts el
        LEFT JOIN FETCH el.venueLayout vl
        LEFT JOIN FETCH el.modification
        WHERE e.id = :eventId
    """)
    Optional<Event> findWithLayoutsById(@Param("eventId") UUID eventId);
}