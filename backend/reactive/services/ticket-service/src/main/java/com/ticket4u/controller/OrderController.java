package com.ticket4u.controller;

import com.ticket4u.dto.CreateCartOrderRequest;
import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.TicketResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.OrderService;
import com.ticket4u.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final TicketService ticketService;

    /**
     * GET /api/v1/orders/{order-id}
     * Retrieves complete order information including ticket summary
     * 
     * @param orderId     provided by path
     * @param userDetails provided by authentication principal
     * @return the order detail if the user owns that order
     */
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderInformation(@PathVariable("order-id") UUID orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        return ResponseEntity.ok(orderService.findOrderOfUserById(userId, orderId));
    }

    /**
     * GET /api/v1/orders/{order-id}/tickets
     * Retrieves all tickets within a specific order
     * 
     * @param orderId     provided by path
     * @param userDetails provided by authentication principal
     * @return detailed ticket response if user owns that order
     */
    @GetMapping("/{order-id}/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketsInOrder(@PathVariable("order-id") UUID orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        return ResponseEntity.ok(orderService.getTicketsByOrderId(userId, orderId));
    }

    // TODO: Convert all reserved tickets into an order
    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createOrderFromCart(@AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateCartOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrderFromCart(userDetails.getUserId(), request));
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order-id") UUID orderId) {
        return null;
    }
}
