package com.ticket4u.core.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Override
    @EntityGraph(value = "Event.withAllEntities")
    Optional<Event> findById(UUID id);

    @Override
    @EntityGraph(value = "Event.withAllEntities")
    List<Event> findAll();

    @EntityGraph(value = "Event.withAllEntities")
    @Query("SELECT e FROM Event e WHERE e.status IN ('PREMIERE', 'SCHEDULED')")
    List<Event> findAllPurchasable();

    @EntityGraph(value = "Event.withAllEntities")
    @Query("SELECT e FROM Event e WHERE e.status IN ('PREMIERE', 'SCHEDULED')")
    List<Event> findAllPurchasable(Pageable pageable);

    /**
     * @return list of events, sorted by earliest session start date
     */
    @Query("""
    SELECT e FROM Event e
    WHERE EXISTS (
        SELECT s FROM EventSession s
        WHERE s.event = e
        AND s.startDate = (
            SELECT MIN(s2.startDate) FROM EventSession s2
            WHERE s2.event = e
        )
        AND s.startDate >= CURRENT_TIMESTAMP
    )
    ORDER BY (
        SELECT MIN(s3.startDate) FROM EventSession s3
        WHERE s3.event = e
        AND s3.startDate >= CURRENT_TIMESTAMP
    ) ASC
    """)
    @EntityGraph(value = "Event.withAllEntities")
    Page<Event> findAllOrderedByStartDate(Pageable pageable);
}
