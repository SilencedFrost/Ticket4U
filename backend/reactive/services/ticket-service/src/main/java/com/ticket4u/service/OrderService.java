package com.ticket4u.service;

import com.ticket4u.dto.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse getOrderOfUserById(UUID userId, UUID orderId);
}
