package com.ticket4u.controller;

import com.ticket4u.dto.OrderResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * GET /api/v1/orders/{order-id}
     * Retrieves complete order information including ticket summary
     * @param orderId provided by path
     * @param userDetails provided by authentication principle
     * @return the order detail if the user owns that order
     */
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderInformation(@PathVariable("order-id") UUID orderId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        return ResponseEntity.ok(orderService.getOrderOfUserById(userId, orderId));
    }

    //TODO:  Retrieves all tickets within a specific order
    @GetMapping("/{order-id}/tickets")
    public ResponseEntity<?> getTicketsInOrder(@PathVariable("order-id") UUID orderId) {
        return null;
    }

    //TODO: Converts a reservation into a purchased order. Only seats in user's reservation can be purchased.
    @PostMapping
    public ResponseEntity<?> createOrder() {
        return null;
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order-id") UUID orderId) {
        return null;
    }
}
