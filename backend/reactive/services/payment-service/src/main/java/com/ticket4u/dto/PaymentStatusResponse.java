package com.ticket4u.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentStatusResponse(
        UUID orderId,
        String paymentStatus,
        String orderStatus,
        String transactionId,
        BigDecimal totalAmount,
        String currency
) {
}
