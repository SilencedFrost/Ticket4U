package com.ticket4u.core.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
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
    @Query("SELECT e FROM Event e WHERE e.status IN ('PREMIERE', 'SELLING')")
    List<Event> findAllPremiereAndSelling();
}
