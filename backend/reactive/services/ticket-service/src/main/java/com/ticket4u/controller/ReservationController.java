package com.ticket4u.controller;

import com.ticket4u.dto.AvailabilityResponse;
import com.ticket4u.dto.ReserveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    //TODO: Retrieves all reservations for authenticated user
    @GetMapping
    public ResponseEntity<?> getReservations() {
        return null;
    }

    //TODO: Retrieves reservation details and marks it as actively being viewed (confirms user intent)
    @GetMapping("/{reservation-id}")
    public ResponseEntity<?> getReserveStatus(@PathVariable("reservation-id") UUID reservationId) {
        return null;
    }

    //TODO: Creates a 15-minute reservation for specified seats
    @PostMapping
    public ResponseEntity<?> reserveSeat(@RequestBody ReserveRequest reserveRequest) {
        return null;
    }

    //TODO: Cancels an active reservation and releases the seats
    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("reservation-id") UUID reservationId) {
        return null;
    }

}
