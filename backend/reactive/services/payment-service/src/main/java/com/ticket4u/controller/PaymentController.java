package com.ticket4u.controller;

import com.ticket4u.dto.CreateVietQrPaymentRequest;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.VietQrPaymentResponse;
import com.ticket4u.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/vietqr")
    public ResponseEntity<VietQrPaymentResponse> createVietQr(@Valid @RequestBody CreateVietQrPaymentRequest request) {
        return ResponseEntity.ok(paymentService.createVietQrPayment(request));
    }

    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(orderId));
    }
}
