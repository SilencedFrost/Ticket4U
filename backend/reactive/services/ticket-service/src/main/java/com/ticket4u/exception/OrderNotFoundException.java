package com.ticket4u.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public  OrderNotFoundException(UUID id) {
        super(String.format("Order not found with id: %s", id.toString()));
    }
}
