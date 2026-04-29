package com.ticket4u.service.impl;

import com.ticket4u.dto.CreateCartOrderRequest;
import com.ticket4u.dto.CreateCartOrderTicketRequest;
import com.ticket4u.dto.InternalOrderPaymentConfirmationRequest;
import com.ticket4u.dto.InternalOrderPaymentSnapshotResponse;
import com.ticket4u.dto.OrderResponse;
import com.ticket4u.dto.TicketResponse;
import com.ticket4u.entity.Order;
import com.ticket4u.entity.Ticket;
import com.ticket4u.exception.OrderNotFoundException;
import com.ticket4u.mapper.OrderMapper;
import com.ticket4u.mapper.TicketMapper;
import com.ticket4u.repository.OrderRepository;
import com.ticket4u.repository.TicketRepository;
import com.ticket4u.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final TicketMapper ticketMapper;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public OrderResponse createOrderFromCart(UUID userId, CreateCartOrderRequest request) {
        OffsetDateTime now = OffsetDateTime.now();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(calculateTotal(request.tickets()));
        order.setCurrency(normalizeCurrency(request.currency()));
        order.setEmail(request.email().trim());
        order.setStatus("PENDING_PAYMENT");
        order.setPaymentMethod(null);
        order.setPaymentStatus("UNPAID");
        order.setTransactionId(null);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        Order savedOrder = orderRepository.save(order);
        List<Ticket> tickets = new ArrayList<>();

        for (CreateCartOrderTicketRequest ticketRequest : request.tickets()) {
            Ticket ticket = new Ticket();
            ticket.setOrder(savedOrder);
            ticket.setEventId(UUID.fromString(request.eventId()));
            ticket.setEventName(request.eventName().trim());
            ticket.setSeatId(UUID.fromString(ticketRequest.seatId()));
            ticket.setSeatName(ticketRequest.seatName().trim());
            ticket.setZoneId(UUID.fromString(ticketRequest.zoneId()));
            ticket.setZoneName(ticketRequest.zoneName().trim());
            ticket.setTicketType(ticketRequest.ticketType().trim().toUpperCase());
            ticket.setBasePrice(ticketRequest.basePrice());
            ticket.setStatus("PENDING");
            ticket.setCreatedAt(now);
            ticket.setQrSecret(UUID.randomUUID().toString().replace("-", ""));
            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);
        savedOrder.getTickets().addAll(tickets);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderResponse findOrderOfUserById(UUID userId, UUID orderId) {
        return orderMapper.toDTO(orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId)));
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
    public InternalOrderPaymentSnapshotResponse confirmPayment(UUID orderId,
            InternalOrderPaymentConfirmationRequest request) {
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
                order.getTransactionId());
    }

    private BigDecimal calculateTotal(List<CreateCartOrderTicketRequest> tickets) {
        return tickets.stream()
                .map(CreateCartOrderTicketRequest::basePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String normalizeCurrency(String currency) {
        String trimmed = currency == null ? "" : currency.trim();
        return trimmed.isEmpty() ? "VND" : trimmed.toUpperCase();
    }
}
