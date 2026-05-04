package com.ticket4u.repository;

import com.ticket4u.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByEventId(UUID id);

    Optional<Ticket> findByIdAndOrderUserId(UUID ticketId, UUID userId);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = :status WHERE t.order.id = :orderId")
    int updateStatusByOrderId(@Param("orderId") UUID orderId, @Param("status") String status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.eventId = :eventId AND t.seatId = :seatId AND t.status = 'ACTIVE'")
    long countActiveBySeatId(@Param("eventId") UUID eventId, @Param("seatId") UUID seatId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.eventId = :eventId AND t.status = 'ACTIVE'")
    long countActiveByEventId(@Param("eventId") UUID eventId);

    @Query("SELECT t FROM Ticket t WHERE t.eventId = :eventId AND t.seatId = :seatId AND t.status = 'ACTIVE'")
    Optional<Ticket> findActiveTicketBySeatId(@Param("eventId") UUID eventId, @Param("seatId") UUID seatId);
}
