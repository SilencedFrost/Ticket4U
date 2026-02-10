package com.ticket4u.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    //TODO: Retrieves complete order information including ticket summary
    @GetMapping("/{order-id}")
    public ResponseEntity<?> getOrderInformation(@PathVariable("order-id") UUID orderId) {
        return null;
    }

    //TODO:  Retrieves all tickets within a specific order
    @GetMapping("/{order-id}/tickets")
    public ResponseEntity<?> getOrderInformationWithTickets(@PathVariable("order-id") UUID orderId) {
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
