package com.ticket4u.service;

import com.ticket4u.dto.CreateSepayPaymentRequest;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.SepayPaymentResponse;

import java.util.UUID;

public interface PaymentService {
    SepayPaymentResponse createSepayPayment(CreateSepayPaymentRequest request);

    PaymentStatusResponse getPaymentStatus(UUID orderId);
}
