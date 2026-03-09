package com.ticket4u.event.controller;

import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.service.EventService;
import com.ticket4u.event.service.EventDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller for viewing event details in the Ticket4U system.
 * This class provides public APIs for users to browse event information.
 */
@RestController
@RequestMapping("/api/v1/public/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventDetailService eventDetailService;

    /**
     * GET /api/v1/public/events/{id}
     * Get full information of a specific event by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.findEvent(id));
    }

    /**
     * GET /api/v1/public/events/{id}/related
     * @param id used to identify relevancy and exclude self
     * @return a list of related event summaries based on category, city and upcoming event.
     */
    @GetMapping("/{id}/related")
    public ResponseEntity<List<EventSummaryResponse>> getRelatedEvents(@PathVariable UUID id) {
        return ResponseEntity.ok(eventDetailService.findRelatedEvents(id));
    }
}
