package com.ticket4u.service.impl;

import com.ticket4u.config.PaymentProperties;
import com.ticket4u.dto.CreateSepayPaymentRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.SepayPaymentResponse;
import com.ticket4u.service.PaymentOrderService;
import com.ticket4u.service.PaymentService;
import com.ticket4u.util.SepayQrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderService paymentOrderService;
    private final PaymentProperties paymentProperties;
    private final SepayQrUtil sepayQrUtil;

    @Override
    public SepayPaymentResponse createSepayPayment(CreateSepayPaymentRequest request) {
        OrderPaymentSnapshotResponse snapshot = paymentOrderService.getOrderPaymentSnapshot(request.orderId());

        String orderCode = buildOrderCode(request.orderId());
        SepayQrUtil.SepayQrDetails sepayQrDetails = sepayQrUtil.build(snapshot.totalAmount(), orderCode);

        return new SepayPaymentResponse(
                snapshot.orderId(),
                orderCode,
                snapshot.totalAmount(),
                snapshot.currency(),
                sepayQrDetails.bankCode(),
                sepayQrDetails.accountNumber(),
                sepayQrDetails.accountName(),
                sepayQrDetails.template(),
                sepayQrDetails.qrUrl(),
                snapshot.paymentStatus(),
                snapshot.orderStatus(),
                snapshot.transactionId());
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(UUID orderId) {
        OrderPaymentSnapshotResponse snapshot = paymentOrderService.getOrderPaymentSnapshot(orderId);

        return new PaymentStatusResponse(
                snapshot.orderId(),
                snapshot.paymentStatus(),
                snapshot.orderStatus(),
                snapshot.transactionId(),
                snapshot.totalAmount(),
                snapshot.currency());
    }

    private String buildOrderCode(UUID orderId) {
        String compactOrderId = orderId.toString().replace("-", "");
        return paymentProperties.resolveOrderCodePrefix() + " " + compactOrderId;
    }
}
