package com.ticket4u.core.service.impl;

import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.mapper.EventMapper;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.core.service.EventService;
import com.ticket4u.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    @Override
    public EventResponse findById(UUID id) {
        return eventRepository.findById(id).map(eventMapper::toDTO).orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    public List<EventSummaryResponse> findAll() {
        return eventRepository.findAll().stream().map(eventMapper::toSummaryDTO).toList();
    }
}
