package com.ticket4u.controller;

import com.ticket4u.dto.SepayWebhookPayload;
import com.ticket4u.dto.WebhookAckResponse;
import com.ticket4u.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/payments")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    @PostMapping("/webhooks/sepay")
    public ResponseEntity<WebhookAckResponse> receiveSepayWebhook(
            @RequestBody SepayWebhookPayload payload,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (!paymentWebhookService.isAuthorizationValid(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new WebhookAckResponse(false, "Unauthorized webhook request"));
        }

        WebhookAckResponse response = paymentWebhookService.processWebhook(payload);
        HttpStatus status = response.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
