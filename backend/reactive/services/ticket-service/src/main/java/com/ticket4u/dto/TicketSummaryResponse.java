package com.ticket4u.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketSummaryResponse(
    UUID id,
    UUID eventId,
    String eventName,
    String seatName,
    String zoneName,
    String ticketType,
    BigDecimal basePrice,
    String status
) {
}
