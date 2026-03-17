package com.ticket4u.eventmanagement.controller;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.eventmanagement.dto.*;
import com.ticket4u.eventmanagement.service.EventManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizer")
@RequiredArgsConstructor
public class EventManagementController {

    private final EventManagementService eventManagementService;

    private UUID currentUserId() {
        return UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    // ── Profile ────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<OrganizerProfileResponse> getProfile() {
        return ResponseEntity.ok(eventManagementService.getProfile(currentUserId()));
    }

    // ── Categories ─────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<List<CategorySummaryResponse>> getCategories() {
        return ResponseEntity.ok(eventManagementService.getCategories());
    }

    // ── Events ─────────────────────────────────────────────

    @GetMapping("/events")
    public ResponseEntity<List<OrganizerEventResponse>> getEvents() {
        return ResponseEntity.ok(eventManagementService.getEvents(currentUserId()));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<OrganizerEventResponse> getEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventManagementService.getEvent(currentUserId(), eventId));
    }

    @PostMapping("/events")
    public ResponseEntity<OrganizerEventResponse> createEvent(
            @Valid @RequestBody OrganizerEventRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventManagementService.createEvent(currentUserId(), request));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<OrganizerEventResponse> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody OrganizerEventRequest request) {
        return ResponseEntity.ok(eventManagementService.updateEvent(currentUserId(), eventId, request));
    }

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventManagementService.deleteEvent(currentUserId(), eventId);
        return ResponseEntity.noContent().build();
    }

    // ── Sessions ───────────────────────────────────────────

    @GetMapping("/events/{eventId}/sessions")
    public ResponseEntity<List<EventSessionResponse>> getSessions(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventManagementService.getSessions(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/sessions/{sessionId}")
    public ResponseEntity<EventSessionResponse> updateSession(
            @PathVariable UUID eventId,
            @PathVariable UUID sessionId,
            @Valid @RequestBody EventSessionRequest request) {
        return ResponseEntity.ok(
                eventManagementService.updateSession(currentUserId(), eventId, sessionId, request));
    }

    // ── Zones ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<List<ZoneManagementResponse>> getZones(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(eventManagementService.getZones(currentUserId(), sessionId));
    }

    @PostMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<ZoneManagementResponse> createZone(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventManagementService.createZone(currentUserId(), sessionId, request));
    }

    @PutMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<ZoneManagementResponse> updateZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity.ok(
                eventManagementService.updateZone(currentUserId(), sessionId, zoneId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<Void> deleteZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        eventManagementService.deleteZone(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }

    // ── Layout ─────────────────────────────────────────────
    // GET /events/{eventId}/layout       → get current layout (venue default or custom)
    // PUT /events/{eventId}/layout       → apply layout and generate seats
    //   body: { "useVenueLayout": true }
    //      or { "useVenueLayout": false, "customLayoutJson": "..." }

    @GetMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> getLayout(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventManagementService.getLayout(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> applyLayout(
            @PathVariable UUID eventId,
            @RequestBody EventLayoutRequest request) {
        return ResponseEntity.ok(eventManagementService.applyLayout(currentUserId(), eventId, request));
    }

    // ── Seats ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        return ResponseEntity.ok(eventManagementService.getSeats(currentUserId(), sessionId, zoneId));
    }

    @PostMapping("/sessions/{sessionId}/zones/{zoneId}/seats/generate")
    public ResponseEntity<List<SeatResponse>> generateSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @RequestBody SeatGenerateRequest request) {
        return ResponseEntity.ok(
                eventManagementService.generateSeats(currentUserId(), sessionId, zoneId, request));
    }

    @PatchMapping("/sessions/{sessionId}/zones/{zoneId}/seats/{seatId}/price")
    public ResponseEntity<SeatResponse> updateSeatPrice(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @PathVariable UUID seatId,
            @RequestBody SeatPriceOverrideRequest request) {
        return ResponseEntity.ok(
                eventManagementService.updateSeatPrice(currentUserId(), sessionId, zoneId, seatId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<Void> deleteSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        eventManagementService.deleteSeats(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }
}