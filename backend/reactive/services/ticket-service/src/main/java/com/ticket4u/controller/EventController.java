package com.ticket4u.controller;

import com.ticket4u.dto.AvailabilityResponse;
import com.ticket4u.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    /**
     * GET /api/v1/public/events/{event-id}/seats/availability
     * Retrieves availability status of seats/slots without revealing reservation ownership
     */
    @GetMapping("/public/events/{event-id}/seats/availability")
    public ResponseEntity<?> getSeatAvailability(@PathVariable("event-id") UUID eventId) {
        return ResponseEntity.ok(eventService.getAvailabilityByServiceId(eventId));
    }
}
