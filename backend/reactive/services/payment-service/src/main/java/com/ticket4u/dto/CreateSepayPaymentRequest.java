package com.ticket4u.dto;

import java.util.UUID;

public record CreateSepayPaymentRequest(
        UUID orderId,
        String userId,
        CartOrderRequest createOrder) {
}
