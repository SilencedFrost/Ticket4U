package com.ticket4u.service.impl;

import com.ticket4u.dto.OrderPaymentConfirmationRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.service.PaymentOrderService;
import com.ticket4u.service.TicketOrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private static final String PAID = "PAID";
    private static final String CONFIRMED = "CONFIRMED";

    private final TicketOrderClient ticketOrderClient;

    @Override
    public OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId) {
        return ticketOrderClient.getOrderPaymentSnapshot(orderId);
    }

    @Override
    public void confirmOrderPaid(UUID orderId, String gateway, String transactionId, String referenceCode) {
        ticketOrderClient.confirmOrderPayment(
                orderId,
                new OrderPaymentConfirmationRequest(
                        PAID,
                        CONFIRMED,
                        gateway,
                        transactionId,
                        OffsetDateTime.now(),
                        referenceCode));
    }
}
