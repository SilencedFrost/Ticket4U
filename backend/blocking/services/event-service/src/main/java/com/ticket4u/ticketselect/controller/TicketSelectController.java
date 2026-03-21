package com.ticket4u.ticketselect.controller;

import com.ticket4u.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.ticketselect.service.TicketSelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/ticket-select")
@RequiredArgsConstructor
public class TicketSelectController {

    private final TicketSelectService ticketSelectService;

    @GetMapping("/{eventId}")
    public ResponseEntity<TicketSelectResponse> getTicketSelectData(
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(ticketSelectService.getTicketSelectData(eventId));
    }
}