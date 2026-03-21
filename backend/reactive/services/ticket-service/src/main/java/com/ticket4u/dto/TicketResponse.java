package com.ticket4u.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        UUID orderId,
        UUID eventId,
        String eventName,
        UUID seatId,
        String seatName,
        UUID zoneId,
        String zoneName,
        String ticketType,
        BigDecimal basePrice,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime usedAt
) {
}
