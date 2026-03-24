package com.ticket4u.ticketselect.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TicketSelectRepository extends JpaRepository<Event, UUID> {

    @Query("""
        SELECT DISTINCT e FROM Event e
        LEFT JOIN FETCH e.sessions s
        LEFT JOIN FETCH s.zones
        LEFT JOIN FETCH e.venue v
        WHERE e.id = :eventId
    """)
    Optional<Event> findWithSessionsZonesAndVenueById(@Param("eventId") UUID eventId);
}