package com.ticket4u.eventlayout.repository;

import com.ticket4u.core.entity.Seat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventLayoutSeatRepository extends JpaRepository<Seat, UUID> {

    @EntityGraph(attributePaths = { "zone" })
    @Query("SELECT s FROM Seat s JOIN s.zone z JOIN z.session sess WHERE sess.id = :sessionId ORDER BY z.id, s.rowName, s.colName")
    List<Seat> findAllBySessionId(@Param("sessionId") UUID sessionId);
}