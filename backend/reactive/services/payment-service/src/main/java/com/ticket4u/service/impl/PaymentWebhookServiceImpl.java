package com.ticket4u.service.impl;

import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.SepayWebhookPayload;
import com.ticket4u.dto.WebhookAckResponse;
import com.ticket4u.service.PaymentOrderService;
import com.ticket4u.service.PaymentWebhookService;
import com.ticket4u.service.PaymentWebhookEventService;
import com.ticket4u.util.PaymentOrderReferenceUtil;
import com.ticket4u.util.PaymentWebhookAuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentWebhookServiceImpl implements PaymentWebhookService {

    private static final String SEPAY_GATEWAY = "SEPAY";

    private final PaymentWebhookAuthorizationUtil paymentWebhookAuthorizationUtil;
    private final PaymentOrderReferenceUtil paymentOrderReferenceUtil;
    private final PaymentOrderService paymentOrderService;
    private final PaymentWebhookEventService paymentWebhookEventService;

    @Override
    public boolean isAuthorizationValid(String authorizationHeader) {
        return paymentWebhookAuthorizationUtil.isAuthorizationValid(authorizationHeader);
    }

    @Override
    public WebhookAckResponse processWebhook(SepayWebhookPayload payload) {
        if (payload == null || payload.id() == null || payload.transferAmount() == null) {
            return failure("Invalid webhook payload");
        }

        if (!isIncomingTransfer(payload.transferType())) {
            return success("Ignored non-incoming transfer");
        }

        if (paymentWebhookEventService.exists(payload.id())) {
            return success("Webhook already processed");
        }

        Optional<UUID> orderIdOptional = paymentOrderReferenceUtil.extractOrderId(payload.content());
        if (orderIdOptional.isEmpty()) {
            return success("Ignored webhook without recognized order code");
        }

        UUID orderId = orderIdOptional.get();

        OrderPaymentSnapshotResponse snapshot;
        try {
            snapshot = paymentOrderService.getOrderPaymentSnapshot(orderId);
        } catch (Exception ex) {
            return failure("Unable to retrieve order for webhook processing");
        }

        if (snapshot == null || snapshot.totalAmount() == null) {
            return failure("Order snapshot is invalid");
        }

        if (snapshot.totalAmount().compareTo(payload.transferAmount()) != 0) {
            return success("Ignored webhook due to amount mismatch");
        }

        if ("PAID".equalsIgnoreCase(snapshot.paymentStatus())) {
            if (!paymentWebhookEventService.saveIfAbsent(payload, orderId)) {
                return success("Webhook already processed");
            }

            return success("Order is already paid");
        }

        try {
            paymentOrderService.confirmOrderPaid(
                    orderId,
                    SEPAY_GATEWAY,
                    payload.id().toString(),
                    payload.referenceCode());
        } catch (Exception ex) {
            return failure("Failed to confirm payment for order");
        }

        if (!paymentWebhookEventService.saveIfAbsent(payload, orderId)) {
            return success("Webhook already processed");
        }

        return success("Webhook processed successfully");
    }

    private boolean isIncomingTransfer(String transferType) {
        return "in".equalsIgnoreCase(transferType);
    }

    private WebhookAckResponse success(String message) {
        return new WebhookAckResponse(true, message);
    }

    private WebhookAckResponse failure(String message) {
        return new WebhookAckResponse(false, message);
    }
}
