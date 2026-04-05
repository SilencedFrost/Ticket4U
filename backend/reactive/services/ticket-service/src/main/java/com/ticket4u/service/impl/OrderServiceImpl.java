package com.ticket4u.service.impl;

import com.ticket4u.dto.InternalOrderPaymentConfirmationRequest;
import com.ticket4u.dto.InternalOrderPaymentSnapshotResponse;
import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.TicketResponse;
import com.ticket4u.entity.Order;
import com.ticket4u.exception.OrderNotFoundException;
import com.ticket4u.mapper.OrderMapper;
import com.ticket4u.mapper.TicketMapper;
import com.ticket4u.repository.OrderRepository;
import com.ticket4u.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

    @Override
    public InternalOrderPaymentSnapshotResponse getPaymentSnapshot(UUID orderId) {
        return toPaymentSnapshot(findOrder(orderId));
    }

    @Override
    public InternalOrderPaymentSnapshotResponse confirmPayment(UUID orderId, InternalOrderPaymentConfirmationRequest request) {
        Order order = findOrder(orderId);

        if ("PAID".equalsIgnoreCase(order.getPaymentStatus())) {
            return toPaymentSnapshot(order);
        }

        order.setPaymentStatus(request.paymentStatus().trim().toUpperCase());
        order.setStatus(request.orderStatus().trim().toUpperCase());
        order.setPaymentMethod(request.paymentMethod().trim().toUpperCase());
        order.setTransactionId(request.sepayTransactionId());
        order.setPurchasedAt(request.paidAt());
        order.setUpdatedAt(OffsetDateTime.now());

        return toPaymentSnapshot(orderRepository.save(order));
    }

    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private InternalOrderPaymentSnapshotResponse toPaymentSnapshot(Order order) {
        return new InternalOrderPaymentSnapshotResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getPaymentStatus(),
                order.getStatus(),
                order.getTransactionId()
        );
    }
}
