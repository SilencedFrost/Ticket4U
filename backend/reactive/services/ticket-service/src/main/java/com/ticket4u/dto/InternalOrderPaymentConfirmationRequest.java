package com.ticket4u.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record InternalOrderPaymentConfirmationRequest(
        @NotBlank String paymentStatus,
        @NotBlank String orderStatus,
        @NotBlank String paymentMethod,
        @NotBlank String sepayTransactionId,
        @NotNull OffsetDateTime paidAt,
        String referenceCode
) {
}
