package com.ticket4u.service.impl;

import com.ticket4u.dto.organizer.OrganizerDTO;
import com.ticket4u.repository.OrganizerRepository;
import com.ticket4u.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    private final OrganizerRepository organizerRepository;

    @Override
    public OrganizerDTO getOrganizerById(UUID id) {
        var organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà tổ chức với ID: " + id));

        return OrganizerDTO.builder()
                .id(organizer.getId())
                .name(organizer.getName())
                .avatar(organizer.getLogo_url())
                .description(organizer.getDescription())
                .build();
    }
}
