package com.ticket4u.service;

import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.TicketResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse findOrderOfUserById(UUID userId, UUID orderId);
    List<TicketResponse> getTicketsByOrderId(UUID userId, UUID orderId);
}
