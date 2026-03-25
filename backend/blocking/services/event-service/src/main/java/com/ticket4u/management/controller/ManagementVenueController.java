package com.ticket4u.management.controller;

import com.ticket4u.management.dto.ManagementVenueResponse;
import com.ticket4u.management.repository.EventManagementVenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events/venues")
@RequiredArgsConstructor
public class ManagementVenueController {

    private final EventManagementVenueRepository venueRepository;

    @GetMapping
    public ResponseEntity<List<ManagementVenueResponse>> getAllVenues() {
        return ResponseEntity.ok(
                venueRepository.findAll().stream()
                        .map(v -> new ManagementVenueResponse(
                                v.getId(),
                                v.getName(),
                                v.getAddressLine(),
                                v.getLongitude(),
                                v.getLatitude(),
                                v.getImageUrl(),
                                v.getLayout()
                        ))
                        .toList()
        );
    }
}