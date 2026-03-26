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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EventDomainServiceImpl implements EventDomainService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EventSemanticService eventSemanticService;

    @Override
    public List<EventSummaryResponse> findRelatedEvents(UUID id) {
        // Fail fast
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        EventResponse originalDTO = eventMapper.toDTO(event);

        int dynamicLimit = RelatedEvents.MAX_COUNT * RelatedEvents.WEIGHTS.FETCH_LIMIT_MULTIPLIER;
        Map<UUID, Float> semanticMap = eventSemanticService.findSimilarEvents(id, PageRequest.of(0, dynamicLimit));

        List<Event> candidates;
        if (semanticMap.isEmpty()) {
            candidates = eventRepository.findAllPurchasable().stream()
                    .filter(e -> !e.getId().equals(id))
                    .toList();
            log.info("Semantic search empty. Falling back to all purchasable events for ranking.");
        } else {
            candidates = eventRepository.findAllById(semanticMap.keySet());
        }

        // Score and sort all events (excluding the original)
        return candidates.stream()
                .map(e -> new ScoredEvent(e, scoreEvent(originalDTO, e, semanticMap)))
                .sorted(Comparator.comparingInt(ScoredEvent::score).reversed())
                .limit(RelatedEvents.MAX_COUNT)
                .map(scoredEvent -> eventMapper.toSummaryDTO(scoredEvent.event))
                .toList();
    }

    // This method returns only relevancy in terms of start date for this build
    private int scoreEvent(EventResponse originalEvent, Event targetEvent, Map<UUID, Float> semanticScoreMap) {
        EventResponse targetEventFlatmap = eventMapper.toDTO(targetEvent);

        float distanceKm = (float) calculateDistance(
                originalEvent.latitude(), originalEvent.longitude(),
                targetEvent.getLatitude(), targetEvent.getLongitude()
        );

        float proximity = proximityToZero(distanceKm, RelatedEvents.WEIGHTS.LOCATION_CUTOFF);

        // Score calculation
        float semanticScore = semanticScoreMap.getOrDefault(targetEvent.getId(), 0.1f);
        float locationScore = transformScore(proximity, RelatedEvents.WEIGHTS.LOCATION_SIGMOID_BIAS, RelatedEvents.WEIGHTS.LOCATION_SIGMOID_WEIGHT, false);
        float dateScore = transformScore(proximityToZero(Math.abs(ChronoUnit.DAYS.between(originalEvent.startDate(), targetEventFlatmap.startDate())), RelatedEvents.WEIGHTS.DATE_CUTOFF), RelatedEvents.WEIGHTS.DATE_SIGMOID_BIAS, RelatedEvents.WEIGHTS.DATE_SIGMOID_WEIGHT, false);
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

    private static double haversine(double val) {
        return Math.sin(val / 2) * Math.sin(val / 2);
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

        double l1 = lat1.doubleValue();
        double ln1 = lon1.doubleValue();
        double l2 = lat2.doubleValue();
        double ln2 = lon2.doubleValue();

        double dLat = Math.toRadians(l2 - l1);
        double dLon = Math.toRadians(ln2 - ln1);

        double a = haversine(dLat) +
                Math.cos(Math.toRadians(l1)) * Math.cos(Math.toRadians(l2)) * haversine(dLon);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RelatedEvents.WEIGHTS.EARTH_RADIUS_KM * c;
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
