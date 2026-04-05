package com.ticket4u.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VietQrPaymentResponse(
        UUID orderId,
        String orderCode,
        BigDecimal amount,
        String currency,
        String bankCode,
        String accountNumber,
        String accountName,
        String qrTemplate,
        String qrUrl,
        String paymentStatus,
        String orderStatus,
        String transactionId
) {
}
