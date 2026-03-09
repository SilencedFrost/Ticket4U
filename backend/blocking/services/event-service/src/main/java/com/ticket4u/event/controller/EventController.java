package com.ticket4u.event.controller;

import com.ticket4u.constants.DefaultParams;
import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.service.EventService;
import com.ticket4u.event.service.EventDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final EventDomainService eventDomainService;

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
        return ResponseEntity.ok(eventDomainService.findRelatedEvents(id));
    }

    /**
     * GET /api/v1/public/events/featured?limit={limit}
     * @param limit the amount of featured events to get, by default is 4
     * @return a list of featured events with limited count
     */
    @GetMapping("/featured")
    public ResponseEntity<List<EventSummaryResponse>> getFeaturedEvents(@RequestParam(required = false, name = "limit",  defaultValue = DefaultParams.FEATURED_COUNT_STRING) int limit) {
        return null;
    }
}
