package com.ticket4u.dto;

public record WebhookAckResponse(
        boolean success,
        String message
) {
}
