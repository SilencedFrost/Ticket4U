package com.ticket4u.event.service.impl;

import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.mapper.EventMapper;
import com.ticket4u.core.repository.EventRepository;
import com.ticket4u.embedding.service.EventSemanticService;
import com.ticket4u.event.constants.RelatedEvents;
import com.ticket4u.event.service.EventDomainService;
import com.ticket4u.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventDomainServiceImpl implements EventDomainService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EventSemanticService eventSemanticService;

    @Override
    public List<EventSummaryResponse> findRelatedEvents(UUID id) {
        // Fail fast
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        List<Event> events = eventRepository.findAllPurchasable(); // Only include premiering and selling events
        // Fail fast
        if(events.isEmpty()) return List.of();

        // Score and sort all events (excluding the original)
        return events.parallelStream()
                .filter(e -> !e.getId().equals(id)) // Exclude the original event
                .map(e -> new ScoredEvent(e, scoreEvent(event, e)))
                .sorted(Comparator.comparingInt(ScoredEvent::score).reversed()) // Highest score first
                .limit(RelatedEvents.MAX_COUNT)
                .map(scoredEvent -> eventMapper.toSummaryDTO(scoredEvent.event))
                .toList();
    }

    // This method returns only relevancy in terms of start date for this build
    private int scoreEvent(Event originalEvent, Event targetEvent) {
        EventResponse originalEventFlatmap = eventMapper.toDTO(originalEvent);
        EventResponse targetEventFlatmap = eventMapper.toDTO(targetEvent);

        float distanceKm = (float) calculateDistance(
                originalEvent.getLatitude(), originalEvent.getLongitude(),
                targetEvent.getLatitude(), targetEvent.getLongitude()
        );

        float proximity = proximityToZero(distanceKm, RelatedEvents.WEIGHTS.LOCATION_CUTOFF);

        // Score calculation
        float semanticScore = eventSemanticService.getSimilarityScore(originalEvent.getId(), targetEvent.getId());
        float locationScore = transformScore(proximity, RelatedEvents.WEIGHTS.LOCATION_SIGMOID_BIAS, RelatedEvents.WEIGHTS.LOCATION_SIGMOID_WEIGHT, false);
        float dateScore = transformScore(proximityToZero(Math.abs(ChronoUnit.DAYS.between(originalEventFlatmap.startDate(), targetEventFlatmap.startDate())), RelatedEvents.WEIGHTS.DATE_CUTOFF), RelatedEvents.WEIGHTS.DATE_SIGMOID_BIAS, RelatedEvents.WEIGHTS.DATE_SIGMOID_WEIGHT, false);
        float suppressionWeight = 1;

        if (locationScore < 0.2 && semanticScore < 0.7) {
            suppressionWeight = 0.5f;
        }

        // Multiply by weights, sum all
        float weightedScore =
                (semanticScore * (RelatedEvents.WEIGHTS.NAME + RelatedEvents.WEIGHTS.CATEGORY)) +
                (locationScore * RelatedEvents.WEIGHTS.LOCATION) +
                (dateScore * RelatedEvents.WEIGHTS.DATE);

        // Suppress if needed
        return Math.round(weightedScore * suppressionWeight);
    }

    /**
     * Transform input through a sigmoid function
     * @param input input value (typically 0 to 1)
     * @param bias shift sigmoid right (positive values delay activation)
     * @param weight steepness of curve (higher = steeper)
     * @param inverse if true, produces decreasing sigmoid
     * @return transformed value between 0 and 1
     */
    private float transformScore(float input, float bias, float weight, boolean inverse) {
        float x = (input - bias) * weight;
        float result = (float) (1 / (1 + Math.exp(-x)));
        return inverse ? 1 - result : result;
    }

    /**
     * Calculate the real-world distance between two points using latitude and longitude.
     * This method uses the Haversine formula to find the distance over the Earth's surface.
     * @param lat1 Latitude of the first point
     * @param lon1 Longitude of the first point
     * @param lat2 Latitude of the second point
     * @param lon2 Longitude of the second point
     * @return The distance in kilometers (km).
     * If any coordinate is null, it returns the default LOCATION_CUTOFF.
     */
    private double calculateDistance(BigDecimal lat1, BigDecimal lon1,
                                     BigDecimal lat2, BigDecimal lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return RelatedEvents.WEIGHTS.LOCATION_CUTOFF;
        }

        double r = 6371;
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }

    /**
     * @return proximity of input number to 0, ranging from 1 to 0 with 1 being closest to 0, will output negative values if input > max
     */
    private float proximityToZero(float input, float max) {
        return 1 - (input/ max);
    }

    private record ScoredEvent(Event event, int score) {}

    @Override
    public List<EventSummaryResponse> findUpcomingPurchasableEventsLimit(Integer limit) {
        if (limit == null || limit <= 0) return List.of();

        List<EventSummaryResponse> results = new ArrayList<>();
        int page = 0;
        // Cap batch size
        int batchSize = Math.min(limit * 2, 50);

        while (results.size() < limit) {
            Pageable pageable = PageRequest.of(page, batchSize);
            Page<Event> eventPage = eventRepository.findUpcomingEvents(pageable);

            // Exit if no more events
            if (eventPage.isEmpty()) break;

            // Filter and map matching events
            List<EventSummaryResponse> batch = eventPage.stream()
                    .filter(e -> e.getStatus() == Event.EventStatus.PREMIERE ||
                            e.getStatus() == Event.EventStatus.SELLING)
                    .map(eventMapper::toSummaryDTO)
                    .toList();

            results.addAll(batch);
            page++;

            // Exit if we've reached the last page
            if (!eventPage.hasNext()) break;
        }

        return results.stream().limit(limit).toList();
    }

    /**
     * @param limit the amount of events to return
     * @param samplingMultiplier the multiplier to the sample space, for example, if you want 1/5th odds, multiplier = 5
     * @return a list of randomly picked events
     */
    @Override
    public List<EventSummaryResponse> findRandomEvent(Integer limit, Integer samplingMultiplier) {
        if(limit < 1 || samplingMultiplier < 1) return List.of();

        List<Event> samplingSpace = eventRepository.findAllPurchasable(PageRequest.of(0, limit * samplingMultiplier));

        if(samplingSpace.size() <= limit) return samplingSpace.stream().map(eventMapper::toSummaryDTO).toList();

        Collections.shuffle(samplingSpace, secureRandom);

        return samplingSpace.stream()
                .limit(Math.min(limit, samplingSpace.size()))
                .map(eventMapper::toSummaryDTO)
                .toList();
    }
}
