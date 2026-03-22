package com.ticket4u.exception;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(UUID id) {
        super(String.format("Ticket not found with id: %s", id.toString()));
    }
}
