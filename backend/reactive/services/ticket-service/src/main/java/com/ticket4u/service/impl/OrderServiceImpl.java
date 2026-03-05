package com.ticket4u.service.impl;

import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.TicketResponse;
import com.ticket4u.exception.OrderNotFoundException;
import com.ticket4u.mapper.OrderMapper;
import com.ticket4u.mapper.TicketMapper;
import com.ticket4u.repository.OrderRepository;
import com.ticket4u.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final TicketMapper ticketMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse findOrderOfUserById(UUID userId, UUID orderId) {
        return orderMapper.toDTO(orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(() -> new OrderNotFoundException(orderId)));
    }

    @Override
    public List<TicketResponse> getTicketsByOrderId(UUID userId, UUID orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId))
                .getTickets().stream().map(ticketMapper::toDTO).toList();
    }
}
