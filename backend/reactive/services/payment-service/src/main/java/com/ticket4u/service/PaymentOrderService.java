package com.ticket4u.service;

import com.ticket4u.dto.OrderPaymentSnapshotResponse;

import java.util.UUID;

public interface PaymentOrderService {
    OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId);

    void confirmOrderPaid(UUID orderId, String gateway, String transactionId, String referenceCode);
}
