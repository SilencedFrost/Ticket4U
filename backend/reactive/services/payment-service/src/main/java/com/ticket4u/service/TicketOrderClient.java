package com.ticket4u.service;

import com.ticket4u.dto.OrderPaymentConfirmationRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.CartOrderRequest;

import java.util.UUID;
import java.util.Map;

public interface TicketOrderClient {
    OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId);

    void confirmOrderPayment(UUID orderId, OrderPaymentConfirmationRequest request);

    CreatedOrderResponse createOrderFromCartInternal(CartOrderRequest request);

    record CreatedOrderResponse(java.util.UUID id) {
    }
}
