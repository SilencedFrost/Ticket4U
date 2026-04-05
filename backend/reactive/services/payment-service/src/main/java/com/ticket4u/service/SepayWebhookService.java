package com.ticket4u.service;

import com.ticket4u.dto.SepayWebhookPayload;
import com.ticket4u.dto.WebhookAckResponse;

public interface SepayWebhookService {
    boolean isAuthorizationValid(String authorizationHeader);

    WebhookAckResponse processWebhook(SepayWebhookPayload payload);
}
