package com.ticket4u.organizer.repository;

import com.ticket4u.core.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizerSeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findAllByZoneIdOrderByRowNameAscColNameAsc(UUID zoneId);

    Optional<Seat> findByIdAndZoneId(UUID id, UUID zoneId);

    // Bridge between layout JSON seat_id and real seat rows
    Optional<Seat> findByZoneIdAndSeatCode(UUID zoneId, String seatCode);

    int countByZoneId(UUID zoneId);

    void deleteAllByZoneId(UUID zoneId);
}