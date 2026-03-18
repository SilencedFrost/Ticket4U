package com.ticket4u.management.repository;

import com.ticket4u.core.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventManagementSeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findAllByZoneIdOrderByRowNameAscColNameAsc(UUID zoneId);

    Optional<Seat> findByIdAndZoneId(UUID id, UUID zoneId);

    int countByZoneId(UUID zoneId);

    // Max numeric col value per zone — used to derive gridCols
    @Query("SELECT MAX(CAST(s.colName AS int)) FROM Seat s WHERE s.zone.id = :zoneId")
    Integer maxColNumberByZoneId(@Param("zoneId") UUID zoneId);

    @Modifying
    @Query("DELETE FROM Seat s WHERE s.zone.id = :zoneId")
    void deleteAllByZoneId(@Param("zoneId") UUID zoneId);
}