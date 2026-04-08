package com.ticket4u.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateSepayPaymentRequest(
        @NotNull UUID orderId) {
}
