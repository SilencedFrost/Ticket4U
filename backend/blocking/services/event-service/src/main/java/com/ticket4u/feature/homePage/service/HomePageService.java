package com.ticket4u.feature.homePage.service;

import com.ticket4u.feature.homePage.dto.CategoryDTO;
import com.ticket4u.feature.homePage.dto.EventCardDTO;
import com.ticket4u.feature.homePage.dto.PlaceDTO;
import com.ticket4u.feature.homePage.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // Featured: Events mới nhất
    public List<EventCardDTO> getFeaturedEvents() {
        List<Object[]> results = eventRepository.findFeaturedEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Special: Events sắp diễn ra trong 7 ngày
    public List<EventCardDTO> getSpecialEvents() {
        List<Object[]> results = eventRepository.findSpecialEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Trending: Random 3 PLANNED/ONGOING events
    public List<EventCardDTO> getTrendingEvents() {
        List<Object[]> results = eventRepository.findTrendingEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Suggested: Random PLANNED/ONGOING events
    public List<EventCardDTO> getSuggestedEvents() {
        List<Object[]> results = eventRepository.findSuggestedEvents();
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Music: Events với category = 'Music'
    public List<EventCardDTO> getMusicEvents() {
        CategoryDTO categoryDTO = new CategoryDTO();
        List<Object[]> results = eventRepository.findEventsByCategory("Âm nhạc (Concert)");
        return results.stream()
                .map(EventCardDTO::new)
                .collect(Collectors.toList());
    }

    // Places: Return empty list for now (future implementation)
    public List<PlaceDTO> getPlaces() {
        return new ArrayList<>();
    }
}
