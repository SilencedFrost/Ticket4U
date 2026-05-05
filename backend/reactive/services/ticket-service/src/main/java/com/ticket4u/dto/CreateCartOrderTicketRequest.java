package com.ticket4u.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCartOrderTicketRequest(
        @NotBlank String zoneId,
        @NotBlank String zoneName,
        @NotBlank String ticketType,
        @NotBlank String seatId,
        @NotBlank String seatName,
        @NotNull BigDecimal basePrice) {
}