package com.ticket4u.controller;

import com.ticket4u.dto.TicketResponse;
import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * GET /api/v1/tickets/{ticket-id}
     * Retrieves individual ticket information
     * @param ticketId the id of the ticket, with UUID
     * @return
     */
    @GetMapping("{ticket-id}")
    public ResponseEntity<TicketResponse> getTicketInformation(@PathVariable("ticket-id") UUID ticketId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();
        return ResponseEntity.ok(ticketService.findTicketOfUserById(userId, ticketId));
    }

    //TODO: Updates ticket status (e.g., mark as used at venue entry, cancel individual ticket)
    @PatchMapping("/{ticket-id}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable("ticket-id") UUID ticketId) {
        return null;
    }
}
