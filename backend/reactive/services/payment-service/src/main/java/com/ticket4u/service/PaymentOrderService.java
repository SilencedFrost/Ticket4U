package com.ticket4u.service;

import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.CartOrderRequest;

import java.util.UUID;

public interface PaymentOrderService {
    OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId);

    void confirmOrderPaid(UUID orderId, String gateway, String transactionId, String referenceCode);

    UUID createOrderFromCart(CartOrderRequest request);
}
