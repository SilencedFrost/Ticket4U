package com.ticket4u.service.impl;

import com.ticket4u.dto.OrderPaymentConfirmationRequest;
import com.ticket4u.dto.OrderPaymentSnapshotResponse;
import com.ticket4u.dto.CreateSepayPaymentRequest;
import com.ticket4u.service.TicketOrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketOrderClientImpl implements TicketOrderClient {

    private final RestClient ticketServiceRestClient;

    @Override
    public OrderPaymentSnapshotResponse getOrderPaymentSnapshot(UUID orderId) {
        return ticketServiceRestClient
                .get()
                .uri("/internal/orders/{orderId}/payment-snapshot", orderId)
                .retrieve()
                .body(OrderPaymentSnapshotResponse.class);
    }

    @Override
    public void confirmOrderPayment(UUID orderId, OrderPaymentConfirmationRequest request) {
        ticketServiceRestClient
                .patch()
                .uri("/internal/orders/{orderId}/payment-confirmation", orderId)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public com.ticket4u.service.TicketOrderClient.CreatedOrderResponse createOrderFromCartInternal(
            com.ticket4u.dto.CartOrderRequest request) {
        var body = ticketServiceRestClient
                .post()
                .uri("/internal/orders/from-cart")
                .body(request)
                .retrieve()
                .body(java.util.Map.class);

        Object idObj = body.get("id");
        java.util.UUID id = idObj == null ? null : java.util.UUID.fromString(idObj.toString());
        return new com.ticket4u.service.TicketOrderClient.CreatedOrderResponse(id);
    }
}
