package com.ticket4u.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderPaymentSnapshotResponse(
        UUID orderId,
        BigDecimal totalAmount,
        String currency,
        String paymentStatus,
        String orderStatus,
        String transactionId
) {
}
