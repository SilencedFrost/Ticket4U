package com.ticket4u.event.service.impl;

import com.ticket4u.core.dto.CategorySummaryResponse;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.ticket4u.event.constants.RelatedEvents.WEIGHTS.LOCATION_CUTOFF;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventDomainServiceImpl implements EventDomainService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EventSemanticService eventSemanticService;

    private List<Event> filterPurchasable(List<Event> events) {
        return events.stream()
                .filter(e -> e.getStatus() == Event.EventStatus.PREMIERE || e.getStatus() == Event.EventStatus.SELLING)
                .toList();
    }

    private List<Event> filterPurchasable(Page<Event> events) {
        return filterPurchasable(events.toList());
    }

    @Override
    public List<EventSummaryResponse> findRelatedEvents(UUID id) {
        // Fail fast
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        EventResponse rootEvent = eventMapper.toDTO(event);

        int dynamicLimit = RelatedEvents.MAX_COUNT * RelatedEvents.FETCH_LIMIT_MULTIPLIER;
        Map<UUID, Float> semanticMap = eventSemanticService.findSimilarEvents(id, PageRequest.of(0, dynamicLimit));

        // Resolve semantic service failure
        List<EventResponse> candidateEvents;
        if (!semanticMap.isEmpty()) {
            candidateEvents = filterPurchasable(eventRepository.findAllById(semanticMap.keySet())).stream().map(eventMapper::toDTO).toList();
        } else {
            log.info("Semantic search empty. Falling back to all purchasable events matching category for ranking.");
            candidateEvents = eventRepository.findAllPurchasableInCategory(rootEvent.categories()
                            .stream()
                            .map(CategorySummaryResponse::id)
                            .toList(), Limit.of(dynamicLimit))
                    .stream()
                    .filter(e -> !e.getId().equals(id))
                    .map(eventMapper::toDTO)
                    .toList();
        }

        // Score and sort all events (excluding the original)
        return candidateEvents.stream()
                .map(candidateEvent -> new ScoredEvent(candidateEvent, scoreEvent(rootEvent, candidateEvent, semanticMap.get(candidateEvent.id()))))
                .sorted(Comparator.comparingInt(ScoredEvent::score).reversed())
                .limit(RelatedEvents.MAX_COUNT)
                .map(scoredEvent -> eventMapper.toSummaryDTO(scoredEvent.event))
                .toList();
    }

    // Score event based on semantic score, location, date, if semantic score is null, falls back to category matching
    private int scoreEvent(EventResponse originalEvent, EventResponse targetEvent, Float semanticSimilarity) {
        log.info("=> Scoring similarity for \"{}\" and \"{}\" with input similarity of: {}", originalEvent.name(), targetEvent.name(), semanticSimilarity);

        float distanceKm = (float) calculateDistance(
                originalEvent.latitude(), originalEvent.longitude(),
                targetEvent.latitude(), targetEvent.longitude()
        );

        // Score calculation
        float locationScore = transformScore(proximityToZero(distanceKm, LOCATION_CUTOFF), RelatedEvents.WEIGHTS.LOCATION_SIGMOID_BIAS, RelatedEvents.WEIGHTS.LOCATION_SIGMOID_WEIGHT, false);
        float dateScore = transformScore(proximityToZero(Math.abs(ChronoUnit.DAYS.between(originalEvent.startDate(), targetEvent.startDate())), RelatedEvents.WEIGHTS.DATE_CUTOFF), RelatedEvents.WEIGHTS.DATE_SIGMOID_BIAS, RelatedEvents.WEIGHTS.DATE_SIGMOID_WEIGHT, false);
        float suppressionWeight = 1;

        if (locationScore < 0.2 && (semanticSimilarity == null || semanticSimilarity < 0.7)) {
            suppressionWeight = 0.5f;
        }

        log.info("Location score: {}; Date score: {}", locationScore, dateScore);

        float weightedPrimaryScore;
        if(semanticSimilarity != null) {
            weightedPrimaryScore = semanticSimilarity * RelatedEvents.WEIGHTS.SEMANTIC;
            log.info("Semantic similarity found, final weighted score: {}", weightedPrimaryScore);
        } else {
            Set<Integer> targetCategories = targetEvent.categories().stream().map(CategorySummaryResponse::id).collect(Collectors.toSet());
            long matchedCount = originalEvent.categories().stream()
                    .filter(category -> targetCategories.contains(category.id()))
                    .count();
            weightedPrimaryScore = matchedCount * RelatedEvents.WEIGHTS.CATEGORY;
            log.info("Semantic not found, matched {} categories, final weighted score: {}", matchedCount, weightedPrimaryScore);
        }

        // Multiply by weights, sum all
        float weightedSecondaryScore = (locationScore * RelatedEvents.WEIGHTS.LOCATION) + (dateScore * RelatedEvents.WEIGHTS.DATE);
        log.info("Final unsuppressed score for \"{}\" and \"{}\": {}; and suppression weight of: {}", originalEvent.name(), targetEvent.name(), weightedPrimaryScore + weightedSecondaryScore, suppressionWeight);

        // Suppress if needed
        return Math.round((weightedPrimaryScore + weightedSecondaryScore) * suppressionWeight);
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
    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return LOCATION_CUTOFF;
        }

        double l1 = lat1.doubleValue();
        double ln1 = lon1.doubleValue();
        double l2 = lat2.doubleValue();
        double ln2 = lon2.doubleValue();

        double dLat = Math.toRadians(l2 - l1);
        double dLon = Math.toRadians(ln2 - ln1);

        double a = haversine(dLat) + Math.cos(Math.toRadians(l1)) * Math.cos(Math.toRadians(l2)) * haversine(dLon);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * @return proximity of input number to 0, ranging from 1 to 0 with 1 being closest to 0, will output negative values if input > max
     */
    private float proximityToZero(float input, float max) {
        return 1 - (input/ max);
    }

    private record ScoredEvent(EventResponse event, int score) {}

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
            List<EventSummaryResponse> batch = filterPurchasable(eventPage).stream().map(eventMapper::toSummaryDTO).toList();

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

        List<Event> samplingSpace = eventRepository.findAllPurchasable(PageRequest.of(0, limit * samplingMultiplier)).toList();

        if(samplingSpace.size() <= limit) return samplingSpace.stream().map(eventMapper::toSummaryDTO).toList();

        Collections.shuffle(samplingSpace, secureRandom);

        return samplingSpace.stream()
                .limit(Math.min(limit, samplingSpace.size()))
                .map(eventMapper::toSummaryDTO)
                .toList();
    }

    /**
     * Search events by semantic query.
     * @param query search keyword
     * @param pageable pagination parameters
     * @return a page of matching events
     */
    @Override
    public List<EventSummaryResponse> searchEvents(String query, Pageable pageable) {
        // Get sorted IDs from AI service
        List<UUID> eventIds = eventSemanticService.search(query, pageable, 0.05f);
        if (eventIds.isEmpty()) return List.of();

        // Fetch event data from the database
        List<Event> events = eventRepository.findAllById(eventIds);

        // Put events into a Map for fast lookup by ID
        Map<UUID, Event> lookupMap = events.stream()
                .collect(Collectors.toMap(Event::getId, e -> e));

        // Map sorted IDs back to DTOs using the lookup map
        return eventIds.stream()
                .map(lookupMap::get)
                .filter(Objects::nonNull)
                .map(eventMapper::toSummaryDTO)
                .toList();
    }

    /**
     * Get a list of events near a specific location.
     * @param lat The latitude of the current location
     * @param lon The longitude of the current location
     * @return A list of events found within the nearby area
     */
    @Override
    public List<EventSummaryResponse> getNearbyEvents(BigDecimal lat, BigDecimal lon) {
        List<UUID> nearbyIds = eventRepository.findNearbyEventIds(lat, lon, LOCATION_CUTOFF);
        if (nearbyIds.isEmpty()) return List.of();

        return eventRepository.findAllById(nearbyIds).stream()
                .map(eventMapper::toSummaryDTO)
                .toList();
    }
}
