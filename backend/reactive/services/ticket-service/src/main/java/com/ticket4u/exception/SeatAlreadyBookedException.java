package com.ticket4u.exception;

import java.util.UUID;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String seatName) {
        super(String.format("Seat %s is already booked", seatName));
    }

    public SeatAlreadyBookedException(UUID eventId, UUID seatId) {
        super(String.format("Seat is already booked for event: %s, seatId: %s", eventId, seatId));
    }
}
