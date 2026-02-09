package com.ticket4u.controller;

import com.ticket4u.dto.organizer.OrganizerDTO;
import com.ticket4u.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizers")
@RequiredArgsConstructor
public class OrganizerController {
    private final OrganizerService organizerService;

    @GetMapping("/{id}")
    public OrganizerDTO getOrganizerById(@PathVariable UUID id) {
        return organizerService.getOrganizerById(id);
    }
}
