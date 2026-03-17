package com.ticket4u.crud.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudEventRepository extends JpaRepository<Event, UUID> {

    @Query("""
        SELECT e FROM Event e
        WHERE e.organizerId = :organizerId
        ORDER BY (
            SELECT MIN(s.startDate) FROM EventSession s WHERE s.event = e
        ) DESC NULLS LAST
    """)
    List<Event> findAllByOrganizerIdOrderByFirstSessionStartDesc(@Param("organizerId") UUID organizerId);

    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    @Query("""
        SELECT COALESCE(SUM(z.quantitySold), 0)
        FROM Zone z JOIN z.session s
        WHERE s.event.id = :eventId
    """)
    Integer sumTicketsSoldByEventId(@Param("eventId") UUID eventId);

    @Query("""
        SELECT COALESCE(SUM(z.capacity), 0)
        FROM Zone z JOIN z.session s
        WHERE s.event.id = :eventId
    """)
    Integer sumCapacityByEventId(@Param("eventId") UUID eventId);

    @Query("""
        SELECT COALESCE(SUM(z.price * z.quantitySold), 0)
        FROM Zone z JOIN z.session s
        WHERE s.event.id = :eventId
    """)
    BigDecimal sumRevenueByEventId(@Param("eventId") UUID eventId);
}