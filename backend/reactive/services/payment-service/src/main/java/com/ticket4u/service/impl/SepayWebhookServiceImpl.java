package com.ticket4u.service.impl;

import com.ticket4u.config.PaymentProperties;
import com.ticket4u.dto.OrderPaymentConfirmationRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.SepayWebhookPayload;
import com.ticket4u.dto.WebhookAckResponse;
import com.ticket4u.entity.PaymentWebhookEvent;
import com.ticket4u.repository.PaymentWebhookEventRepository;
import com.ticket4u.service.SepayWebhookService;
import com.ticket4u.service.TicketOrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SepayWebhookServiceImpl implements SepayWebhookService {

    private static final Pattern UUID_PATTERN = Pattern.compile("(?i)\\b([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\\b");

    private final PaymentProperties paymentProperties;
    private final TicketOrderClient ticketOrderClient;
    private final PaymentWebhookEventRepository paymentWebhookEventRepository;

    @Override
    public boolean isAuthorizationValid(String authorizationHeader) {
        String webhookKey = paymentProperties.getWebhookKey();
        if (!StringUtils.hasText(webhookKey)) {
            return true;
        }

        if (!StringUtils.hasText(authorizationHeader)) {
            return false;
        }

        String value = authorizationHeader.trim();
        if (!value.regionMatches(true, 0, "Apikey ", 0, 7)) {
            return false;
        }

        String receivedKey = value.substring(7).trim();
        return webhookKey.equals(receivedKey);
    }

    @Override
    public WebhookAckResponse processWebhook(SepayWebhookPayload payload) {
        if (payload == null || payload.id() == null || payload.transferAmount() == null) {
            return failure("Invalid webhook payload");
        }

        if (!isIncomingTransfer(payload.transferType())) {
            return success("Ignored non-incoming transfer");
        }

        if (paymentWebhookEventRepository.existsById(payload.id())) {
            return success("Webhook already processed");
        }

        Optional<UUID> orderIdOptional = extractOrderId(payload.content());
        if (orderIdOptional.isEmpty()) {
            return success("Ignored webhook without recognized order code");
        }

        UUID orderId = orderIdOptional.get();

        OrderPaymentSnapshotResponse snapshot;
        try {
            snapshot = ticketOrderClient.getOrderPaymentSnapshot(orderId);
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
            if (!saveWebhookEventSafely(payload, orderId)) {
                return success("Webhook already processed");
            }

            return success("Order is already paid");
        }

        try {
            ticketOrderClient.confirmOrderPayment(
                    orderId,
                    new OrderPaymentConfirmationRequest(
                            "PAID",
                            "CONFIRMED",
                            "SEPAY",
                            payload.id().toString(),
                            OffsetDateTime.now(),
                            payload.referenceCode()
                    )
            );
        } catch (Exception ex) {
            return failure("Failed to confirm payment for order");
        }

        if (!saveWebhookEventSafely(payload, orderId)) {
            return success("Webhook already processed");
        }

        return success("Webhook processed successfully");
    }

    private Optional<UUID> extractOrderId(String content) {
        if (!StringUtils.hasText(content)) {
            return Optional.empty();
        }

        String prefix = paymentProperties.resolveOrderCodePrefix();

        Pattern prefixedPattern = Pattern.compile("(?i)\\b" + Pattern.quote(prefix) + "-([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\\b");
        Matcher prefixedMatcher = prefixedPattern.matcher(content);

        if (prefixedMatcher.find()) {
            return parseUuid(prefixedMatcher.group(1));
        }

        Matcher plainMatcher = UUID_PATTERN.matcher(content);
        if (plainMatcher.find()) {
            return parseUuid(plainMatcher.group(1));
        }

        return Optional.empty();
    }

    private Optional<UUID> parseUuid(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private boolean isIncomingTransfer(String transferType) {
        return "in".equalsIgnoreCase(transferType);
    }

    private boolean saveWebhookEventSafely(SepayWebhookPayload payload, UUID orderId) {
        try {
            saveWebhookEvent(payload, orderId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    private void saveWebhookEvent(SepayWebhookPayload payload, UUID orderId) {
        PaymentWebhookEvent event = new PaymentWebhookEvent();
        event.setId(payload.id());
        event.setOrderId(orderId);
        event.setTransferType(payload.transferType());
        event.setTransferAmount(payload.transferAmount());
        event.setGateway(payload.gateway());
        event.setReferenceCode(payload.referenceCode());
        event.setContent(truncate(payload.content(), 1024));
        event.setReceivedAt(OffsetDateTime.now());
        paymentWebhookEventRepository.save(event);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength);
    }

    private WebhookAckResponse success(String message) {
        return new WebhookAckResponse(true, message);
    }

    private WebhookAckResponse failure(String message) {
        return new WebhookAckResponse(false, message);
    }
}
