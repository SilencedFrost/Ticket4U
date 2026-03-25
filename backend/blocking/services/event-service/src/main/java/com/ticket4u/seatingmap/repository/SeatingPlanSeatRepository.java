package com.ticket4u.seatingmap.repository;

import com.ticket4u.core.entity.Seat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatingPlanSeatRepository extends JpaRepository<Seat, UUID> {

    @EntityGraph(attributePaths = { "zone" })
    List<Seat> findAllBySessionId(UUID sessionId);
}