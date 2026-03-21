package com.ticket4u.management.controller;

import com.ticket4u.management.repository.EventManagementVenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events/venues")
@RequiredArgsConstructor
public class ManagementVenueController {

    private final EventManagementVenueRepository venueRepository;

    record VenueResponse(
            UUID id,
            String name,
            String addressLine,
            BigDecimal longitude,
            BigDecimal latitude,
            String imageUrl,
            String layout
    ) {}

    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<VenueResponse> venues = venueRepository.findAll().stream()
                .map(v -> new VenueResponse(
                        v.getId(),
                        v.getName(),
                        v.getAddressLine(),
                        v.getLongitude(),
                        v.getLatitude(),
                        v.getImageUrl(),
                        v.getLayout()
                ))
                .toList();
        return ResponseEntity.ok(venues);
    }
}