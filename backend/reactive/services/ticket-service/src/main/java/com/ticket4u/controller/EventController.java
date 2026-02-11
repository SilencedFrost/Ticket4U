package com.ticket4u.controller;

import com.ticket4u.dto.AvailabilityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    //TODO: Retrieves availability status of seats/slots without revealing reservation ownership
    @GetMapping("/{event-id}/seats/availability")
    public ResponseEntity<AvailabilityResponse> getSeatAvailability(@PathVariable("event-id") UUID eventId) {
        return null;
    }
}
