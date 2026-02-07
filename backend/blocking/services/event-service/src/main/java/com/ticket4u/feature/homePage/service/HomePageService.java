package com.ticket4u.feature.homePage.service;

import com.ticket4u.feature.homePage.dto.EventCardDTO;
import com.ticket4u.feature.homePage.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageService {
    @Autowired
    EventRepository eventRepository;
    
    // Lấy tất cả events với giá thấp nhất
    public List<EventCardDTO> getAllEventsWithMinPrice() {
        List<Object[]> results = eventRepository.findEventsWithMinPrice();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }
    
    // Lấy giá thấp nhất của 1 event cụ thể
    public Double getMinPriceForEvent(UUID eventId) {
        Double minPrice = eventRepository.findMinPriceByEventId(eventId);
        return minPrice != null ? minPrice : 0.0;
    }
}
