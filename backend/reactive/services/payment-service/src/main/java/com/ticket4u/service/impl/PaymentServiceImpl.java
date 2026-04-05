package com.ticket4u.service.impl;

import com.ticket4u.config.PaymentProperties;
import com.ticket4u.dto.CreateVietQrPaymentRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.VietQrPaymentResponse;
import com.ticket4u.service.PaymentService;
import com.ticket4u.service.TicketOrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TicketOrderClient ticketOrderClient;
    private final PaymentProperties paymentProperties;

    @Override
    public VietQrPaymentResponse createVietQrPayment(CreateVietQrPaymentRequest request) {
        OrderPaymentSnapshotResponse snapshot = ticketOrderClient.getOrderPaymentSnapshot(request.orderId());
        validateVietQrConfig();

        String orderCode = buildOrderCode(request.orderId());
        String qrUrl = buildQrUrl(
                paymentProperties.getVietqr().getAccountNumber(),
                paymentProperties.getVietqr().getBankCode(),
                snapshot.totalAmount(),
                orderCode,
                paymentProperties.getVietqr().getTemplate()
        );

        return new VietQrPaymentResponse(
                snapshot.orderId(),
                orderCode,
                snapshot.totalAmount(),
                snapshot.currency(),
                paymentProperties.getVietqr().getBankCode(),
                paymentProperties.getVietqr().getAccountNumber(),
                paymentProperties.getVietqr().getAccountName(),
                paymentProperties.getVietqr().getTemplate(),
                qrUrl,
                snapshot.paymentStatus(),
                snapshot.orderStatus(),
                snapshot.transactionId()
        );
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(UUID orderId) {
        OrderPaymentSnapshotResponse snapshot = ticketOrderClient.getOrderPaymentSnapshot(orderId);

        return new PaymentStatusResponse(
                snapshot.orderId(),
                snapshot.paymentStatus(),
                snapshot.orderStatus(),
                snapshot.transactionId(),
                snapshot.totalAmount(),
                snapshot.currency()
        );
    }

    private void validateVietQrConfig() {
        if (!StringUtils.hasText(paymentProperties.getVietqr().getAccountNumber())) {
            throw new IllegalStateException("Missing SEPAY_QR_ACCOUNT_NUMBER configuration");
        }

        if (!StringUtils.hasText(paymentProperties.getVietqr().getBankCode())) {
            throw new IllegalStateException("Missing SEPAY_QR_BANK_CODE configuration");
        }
    }

    private String buildOrderCode(UUID orderId) {
        return paymentProperties.resolveOrderCodePrefix() + "-" + orderId;
    }

    private String buildQrUrl(String accountNumber, String bankCode, BigDecimal amount, String orderCode, String template) {
        String amountValue = amount.stripTrailingZeros().toPlainString();

        return UriComponentsBuilder.fromUriString("https://qr.sepay.vn/img")
                .queryParam("acc", accountNumber)
                .queryParam("bank", bankCode)
                .queryParam("amount", amountValue)
                .queryParam("des", orderCode)
                .queryParam("template", template)
                .build()
                .toUriString();
    }
}
