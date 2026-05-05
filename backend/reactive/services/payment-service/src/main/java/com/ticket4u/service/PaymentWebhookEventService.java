package com.ticket4u.service;

import com.ticket4u.dto.SepayWebhookPayload;

import java.util.UUID;

public interface PaymentWebhookEventService {
    boolean exists(Long eventId);

    boolean saveIfAbsent(SepayWebhookPayload payload, UUID orderId);
}
