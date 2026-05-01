package com.ticket4u.controller;

import com.ticket4u.dto.CreateSepayPaymentRequest;
import com.ticket4u.dto.PaymentStatusResponse;
import com.ticket4u.dto.SepayPaymentResponse;
import com.ticket4u.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/sepay")
    public ResponseEntity<SepayPaymentResponse> createSepay(@Valid @RequestBody CreateSepayPaymentRequest request) {
        log.debug("Received SePay payment request: {}", request);
        SepayPaymentResponse response = paymentService.createSepayPayment(request);
        log.debug("Created SePay payment successfully for orderId={}", response.orderId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(orderId));
    }
}
