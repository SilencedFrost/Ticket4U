package com.ticket4u.feature.ticketselect.controller;

import com.ticket4u.feature.ticketselect.dto.TicketSelectResponse;
import com.ticket4u.feature.ticketselect.service.TicketSelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ticket-select")
@RequiredArgsConstructor
public class TicketSelectController {

    private final TicketSelectService ticketSelectService;

    @GetMapping
    public ResponseEntity<TicketSelectResponse> getTicketSelectData(
            @RequestParam UUID eventId
    ) {
        TicketSelectResponse response = ticketSelectService.getTicketSelectData(eventId);
        return ResponseEntity.ok(response);
    }
}