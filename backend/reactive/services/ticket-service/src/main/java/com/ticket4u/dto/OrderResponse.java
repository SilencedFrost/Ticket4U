package com.ticket4u.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        BigDecimal totalAmount,
        String discountCode,
        BigDecimal discountAmount,
        BigDecimal fees,
        String status,
        String paymentMethod,
        String paymentStatus,
        String transactionId,
        OffsetDateTime createdAt,
        OffsetDateTime purchasedAt,
        OffsetDateTime updatedAt,
        OffsetDateTime cancelledAt,
        BigDecimal refundAmount,
        String notes,
        List<TicketSummaryResponse> ticketSummaries
) {
}
