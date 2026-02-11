package com.ticket4u.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    //TODO: Retrieves individual ticket information
    @GetMapping("{ticket-id}")
    public ResponseEntity<?> getTicketInformation(@PathVariable("ticket-id") UUID ticketId) {
        return null;
    }

    //TODO: Updates ticket status (e.g., mark as used at venue entry, cancel individual ticket)
    @PatchMapping("/{ticket-id}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable("ticket-id") UUID ticketId) {
        return null;
    }
}
