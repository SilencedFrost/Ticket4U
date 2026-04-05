package com.ticket4u.service;

import com.ticket4u.dto.OrderPaymentConfirmationRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;

import java.util.UUID;

public interface TicketOrderClient {
    OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId);

    void confirmOrderPayment(UUID orderId, OrderPaymentConfirmationRequest request);
}
