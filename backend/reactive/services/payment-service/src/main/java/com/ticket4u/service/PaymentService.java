package com.ticket4u.service;

import com.ticket4u.dto.CreateVietQrPaymentRequest;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.VietQrPaymentResponse;

import java.util.UUID;

public interface PaymentService {
    VietQrPaymentResponse createVietQrPayment(CreateVietQrPaymentRequest request);

    PaymentStatusResponse getPaymentStatus(UUID orderId);
}
