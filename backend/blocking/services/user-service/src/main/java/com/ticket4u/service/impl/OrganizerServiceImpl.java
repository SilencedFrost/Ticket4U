package com.ticket4u.service.impl;

import com.ticket4u.dto.organizer.OrganizerDTO;
import com.ticket4u.mapper.OrganizerMapper;
import com.ticket4u.repository.OrganizerRepository;
import com.ticket4u.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final OrganizerMapper organizerMapper;

    @Override
    public OrganizerDTO getOrganizerById(UUID id) {
        return organizerRepository.findById(id)
                .map(organizerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà tổ chức với ID: " + id));
    }
}
