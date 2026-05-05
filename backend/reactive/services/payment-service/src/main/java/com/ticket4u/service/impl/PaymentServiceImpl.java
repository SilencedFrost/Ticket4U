package com.ticket4u.service.impl;

import com.ticket4u.config.PaymentProperties;
import com.ticket4u.dto.CreateSepayPaymentRequest;
import com.ticket4u.dto.CartOrderRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.SepayPaymentResponse;
import com.ticket4u.service.PaymentOrderService;
import com.ticket4u.service.PaymentService;
import com.ticket4u.util.SepayQrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderService paymentOrderService;
    private final PaymentProperties paymentProperties;
    private final SepayQrUtil sepayQrUtil;

    @Override
    public SepayPaymentResponse createSepayPayment(CreateSepayPaymentRequest request) {
        UUID orderId = request.orderId();
        String userId = request.userId();
        log.debug("Processing SePay payment. orderId={}, hasCreateOrder={}, userId={}", orderId,
                request.createOrder() != null, userId);

        if (orderId == null && request.createOrder() != null) {
            log.debug("No orderId provided. Creating order from cart for email={}, userId={}",
                    request.createOrder().email(), userId);
            orderId = paymentOrderService.createOrderFromCart(request.createOrder(), userId);
            log.debug("Created internal order from cart. orderId={}, userId={}", orderId, userId);
        }

        log.debug("Fetching payment snapshot for orderId={}", orderId);
        OrderPaymentSnapshotResponse snapshot = paymentOrderService.getOrderPaymentSnapshot(orderId);
        log.debug(
                "Fetched payment snapshot. orderId={}, totalAmount={}, currency={}, paymentStatus={}, orderStatus={}, transactionId={}",
                snapshot.orderId(), snapshot.totalAmount(), snapshot.currency(), snapshot.paymentStatus(),
                snapshot.orderStatus(), snapshot.transactionId());

        String orderCode = buildOrderCode(orderId);
        log.debug("Building SePay QR. orderId={}, orderCode={}", orderId, orderCode);
        SepayQrUtil.SepayQrDetails sepayQrDetails = sepayQrUtil.build(snapshot.totalAmount(), orderCode);

        log.debug("Built SePay QR successfully. orderId={}, bankCode={}, accountNumber={}, template={}",
                orderId, sepayQrDetails.bankCode(), sepayQrDetails.accountNumber(), sepayQrDetails.template());

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
