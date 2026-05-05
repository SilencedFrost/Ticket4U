package com.ticket4u.service.impl;

import com.ticket4u.dto.SepayWebhookPayload;
import com.ticket4u.entity.PaymentWebhookEvent;
import com.ticket4u.repository.PaymentWebhookEventRepository;
import com.ticket4u.service.PaymentWebhookEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentWebhookEventServiceImpl implements PaymentWebhookEventService {

    private static final int CONTENT_MAX_LENGTH = 1024;

    private final PaymentWebhookEventRepository paymentWebhookEventRepository;

    @Override
    public boolean exists(Long eventId) {
        return paymentWebhookEventRepository.existsById(eventId);
    }

    @Override
    public boolean saveIfAbsent(SepayWebhookPayload payload, UUID orderId) {
        try {
            save(payload, orderId);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    private void save(SepayWebhookPayload payload, UUID orderId) {
        PaymentWebhookEvent event = new PaymentWebhookEvent();
        event.setId(payload.id());
        event.setOrderId(orderId);
        event.setTransferType(payload.transferType());
        event.setTransferAmount(payload.transferAmount());
        event.setGateway(payload.gateway());
        event.setReferenceCode(payload.referenceCode());
        event.setContent(truncate(payload.content()));
        event.setReceivedAt(OffsetDateTime.now());
        paymentWebhookEventRepository.save(event);
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() <= CONTENT_MAX_LENGTH) {
            return value;
        }

        return value.substring(0, CONTENT_MAX_LENGTH);
    }
}
