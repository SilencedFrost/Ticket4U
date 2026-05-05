package com.ticket4u.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CartOrderRequest(
        String userId,
        @NotBlank String email,
        @NotBlank String currency,
        @NotBlank String eventId,
        @NotBlank String eventName,
        @NotEmpty @Valid List<CartOrderTicketRequest> tickets) {

    public record CartOrderTicketRequest(
            @NotBlank String zoneId,
            @NotBlank String zoneName,
            @NotBlank String ticketType,
            @NotBlank String seatId,
            @NotBlank String seatName,
            java.math.BigDecimal basePrice) {
    }
}
