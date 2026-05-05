package com.ticket4u.controller;

import com.ticket4u.config.InternalApiProperties;
import com.ticket4u.dto.InternalOrderPaymentConfirmationRequest;
import com.ticket4u.dto.InternalOrderPaymentSnapshotResponse;
import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.CreateCartOrderRequest;
import com.ticket4u.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/orders")
@RequiredArgsConstructor
public class InternalOrderController {

    private final OrderService orderService;
    private final InternalApiProperties internalApiProperties;

    @GetMapping("/{orderId}/payment-snapshot")
    public ResponseEntity<InternalOrderPaymentSnapshotResponse> getPaymentSnapshot(
            @PathVariable UUID orderId,
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(orderService.getPaymentSnapshot(orderId));
    }

    @PatchMapping("/{orderId}/payment-confirmation")
    public ResponseEntity<InternalOrderPaymentSnapshotResponse> confirmPayment(
            @PathVariable UUID orderId,
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @Valid @RequestBody InternalOrderPaymentConfirmationRequest request) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(orderService.confirmPayment(orderId, request));
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createOrderFromCartInternal(
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @Valid @RequestBody CreateCartOrderRequest request) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(orderService.createOrderFromCartForSystem(request));
    }

    private void validateApiKey(String apiKey) {
        if (!StringUtils.hasText(apiKey) || !apiKey.equals(internalApiProperties.getApiKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal API key");
        }
    }
}
