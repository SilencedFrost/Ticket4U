package com.ticket4u.organizer.controller;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.organizer.dto.*;
import com.ticket4u.organizer.service.OrganizerService;
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
public class OrganizerController {

    private final OrganizerService organizerService;

    private UUID currentUserId() {
        return UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    // ── Profile ────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<OrganizerProfileResponse> getProfile() {
        return ResponseEntity.ok(organizerService.getProfile(currentUserId()));
    }

    // ── Categories ─────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<List<CategorySummaryResponse>> getCategories() {
        return ResponseEntity.ok(organizerService.getCategories());
    }

    // ── Events ─────────────────────────────────────────────

    @GetMapping("/events")
    public ResponseEntity<List<OrganizerEventResponse>> getEvents() {
        return ResponseEntity.ok(organizerService.getEvents(currentUserId()));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<OrganizerEventResponse> getEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(organizerService.getEvent(currentUserId(), eventId));
    }

    @PostMapping("/events")
    public ResponseEntity<OrganizerEventResponse> createEvent(
            @Valid @RequestBody OrganizerEventRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizerService.createEvent(currentUserId(), request));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<OrganizerEventResponse> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody OrganizerEventRequest request) {
        return ResponseEntity.ok(organizerService.updateEvent(currentUserId(), eventId, request));
    }

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        organizerService.deleteEvent(currentUserId(), eventId);
        return ResponseEntity.noContent().build();
    }

    // ── Sessions ───────────────────────────────────────────

    @GetMapping("/events/{eventId}/sessions")
    public ResponseEntity<List<EventSessionResponse>> getSessions(@PathVariable UUID eventId) {
        return ResponseEntity.ok(organizerService.getSessions(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/sessions/{sessionId}")
    public ResponseEntity<EventSessionResponse> updateSession(
            @PathVariable UUID eventId,
            @PathVariable UUID sessionId,
            @Valid @RequestBody EventSessionRequest request) {
        return ResponseEntity.ok(
                organizerService.updateSession(currentUserId(), eventId, sessionId, request));
    }

    // ── Zones ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<List<OrganizerZoneResponse>> getZones(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(organizerService.getZones(currentUserId(), sessionId));
    }

    @PostMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<OrganizerZoneResponse> createZone(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizerService.createZone(currentUserId(), sessionId, request));
    }

    @PutMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<OrganizerZoneResponse> updateZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity.ok(
                organizerService.updateZone(currentUserId(), sessionId, zoneId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<Void> deleteZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        organizerService.deleteZone(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }

    // ── Layout ─────────────────────────────────────────────
    // GET /events/{eventId}/layout       → get current layout (venue default or custom)
    // PUT /events/{eventId}/layout       → apply layout and generate seats
    //   body: { "useVenueLayout": true }
    //      or { "useVenueLayout": false, "customLayoutJson": "..." }

    @GetMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> getLayout(@PathVariable UUID eventId) {
        return ResponseEntity.ok(organizerService.getLayout(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> applyLayout(
            @PathVariable UUID eventId,
            @RequestBody EventLayoutRequest request) {
        return ResponseEntity.ok(organizerService.applyLayout(currentUserId(), eventId, request));
    }

    // ── Seats ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        return ResponseEntity.ok(organizerService.getSeats(currentUserId(), sessionId, zoneId));
    }

    @PostMapping("/sessions/{sessionId}/zones/{zoneId}/seats/generate")
    public ResponseEntity<List<SeatResponse>> generateSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @RequestBody SeatGenerateRequest request) {
        return ResponseEntity.ok(
                organizerService.generateSeats(currentUserId(), sessionId, zoneId, request));
    }

    @PatchMapping("/sessions/{sessionId}/zones/{zoneId}/seats/{seatId}/price")
    public ResponseEntity<SeatResponse> updateSeatPrice(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @PathVariable UUID seatId,
            @RequestBody SeatPriceOverrideRequest request) {
        return ResponseEntity.ok(
                organizerService.updateSeatPrice(currentUserId(), sessionId, zoneId, seatId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<Void> deleteSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        organizerService.deleteSeats(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }
}