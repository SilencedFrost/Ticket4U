package com.ticket4u.management.controller;

import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.management.dto.*;
import com.ticket4u.management.service.ManagementService;
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

    private final ManagementService managementService;

    private UUID currentUserId() {
        return UUID.fromString(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    // ── Profile ────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<OrganizerProfileResponse> getProfile() {
        return ResponseEntity.ok(managementService.getProfile(currentUserId()));
    }

    // ── Categories ─────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<List<CategorySummaryResponse>> getCategories() {
        return ResponseEntity.ok(managementService.getCategories());
    }

    // ── Events ─────────────────────────────────────────────

    @GetMapping("/events")
    public ResponseEntity<List<ManagementEventResponse>> getEvents() {
        return ResponseEntity.ok(managementService.getEvents(currentUserId()));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<ManagementEventResponse> getEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(managementService.getEvent(currentUserId(), eventId));
    }

    @PostMapping("/events")
    public ResponseEntity<ManagementEventResponse> createEvent(
            @Valid @RequestBody ManagementEventRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(managementService.createEvent(currentUserId(), request));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<ManagementEventResponse> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody ManagementEventRequest request) {
        return ResponseEntity.ok(managementService.updateEvent(currentUserId(), eventId, request));
    }

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        managementService.deleteEvent(currentUserId(), eventId);
        return ResponseEntity.noContent().build();
    }

    // ── Sessions ───────────────────────────────────────────

    @GetMapping("/events/{eventId}/sessions")
    public ResponseEntity<List<EventSessionResponse>> getSessions(@PathVariable UUID eventId) {
        return ResponseEntity.ok(managementService.getSessions(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/sessions/{sessionId}")
    public ResponseEntity<EventSessionResponse> updateSession(
            @PathVariable UUID eventId,
            @PathVariable UUID sessionId,
            @Valid @RequestBody EventSessionRequest request) {
        return ResponseEntity.ok(
                managementService.updateSession(currentUserId(), eventId, sessionId, request));
    }

    // ── Zones ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<List<ManagementZoneResponse>> getZones(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(managementService.getZones(currentUserId(), sessionId));
    }

    @PostMapping("/sessions/{sessionId}/zones")
    public ResponseEntity<ManagementZoneResponse> createZone(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(managementService.createZone(currentUserId(), sessionId, request));
    }

    @PutMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<ManagementZoneResponse> updateZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @Valid @RequestBody ZoneRequest request) {
        return ResponseEntity.ok(
                managementService.updateZone(currentUserId(), sessionId, zoneId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}")
    public ResponseEntity<Void> deleteZone(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        managementService.deleteZone(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }

    // ── Layout ─────────────────────────────────────────────

    @GetMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> getLayout(@PathVariable UUID eventId) {
        return ResponseEntity.ok(managementService.getLayout(currentUserId(), eventId));
    }

    @PutMapping("/events/{eventId}/layout")
    public ResponseEntity<EventLayoutResponse> applyLayout(
            @PathVariable UUID eventId,
            @RequestBody EventLayoutRequest request) {
        return ResponseEntity.ok(managementService.applyLayout(currentUserId(), eventId, request));
    }

    // ── Seats ──────────────────────────────────────────────

    @GetMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        return ResponseEntity.ok(managementService.getSeats(currentUserId(), sessionId, zoneId));
    }

    @PostMapping("/sessions/{sessionId}/zones/{zoneId}/seats/generate")
    public ResponseEntity<List<SeatResponse>> generateSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @RequestBody SeatGenerateRequest request) {
        return ResponseEntity.ok(
                managementService.generateSeats(currentUserId(), sessionId, zoneId, request));
    }

    @PatchMapping("/sessions/{sessionId}/zones/{zoneId}/seats/{seatId}/price")
    public ResponseEntity<SeatResponse> updateSeatPrice(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId,
            @PathVariable UUID seatId,
            @RequestBody SeatPriceOverrideRequest request) {
        return ResponseEntity.ok(
                managementService.updateSeatPrice(currentUserId(), sessionId, zoneId, seatId, request));
    }

    @DeleteMapping("/sessions/{sessionId}/zones/{zoneId}/seats")
    public ResponseEntity<Void> deleteSeats(
            @PathVariable UUID sessionId,
            @PathVariable UUID zoneId) {
        managementService.deleteSeats(currentUserId(), sessionId, zoneId);
        return ResponseEntity.noContent().build();
    }
}