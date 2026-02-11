package com.ticket4u.service;

import com.ticket4u.dto.organizer.OrganizerDTO;

import java.util.UUID;

public interface OrganizerService {
    OrganizerDTO getOrganizerById(UUID id);
}
