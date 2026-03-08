package com.ticket4u.core.repository;

import com.ticket4u.core.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Override
    @EntityGraph(value = "Event.withAllEntities")
    Optional<Event> findById(UUID id);

    @Override
    @EntityGraph(value = "Event.withAllEntities")
    List<Event> findAll();

    @EntityGraph(value = "Event.withAllEntities")
    @Query("SELECT e FROM Event e WHERE e.status IN ('PREMIERE', 'SELLING')")
    List<Event> findAllPremiereAndSelling();

    // ─── Homepage: List & Pricing ─────────────────────────────────────────────

    /**
     * Paginated list of PLANNED/ONGOING events with each event's minimum zone price.
     * Used by: homepage feature
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, NULL AS categoryName
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY MIN(es.start_date) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<EventWithCategoryDto> findEventsWithMinPrice(
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);

    /**
     * Returns the cheapest zone price for a single event across all its sessions.
     * Used by: homepage feature
     */
    @Query(value = """
            SELECT MIN(z.price)
            FROM event_sessions es
            JOIN zones z ON es.id = z.session_id
            WHERE es.event_id = :eventId
            """, nativeQuery = true)
    Double findMinPriceByEventId(@Param("eventId") UUID eventId);

    // ─── Homepage: Curated Sections ───────────────────────────────────────────

    /**
     * Events ordered by creation date descending — newest first.
     * The caller decides what business concept to attach (e.g. "featured" = findLatestEvents(10)).
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, NULL AS categoryName
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY e.created_at DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<EventWithCategoryDto> findLatestEvents(@Param("limit") int limit);

    /**
     * Events whose next session falls within [{@code from}, {@code to}], ordered by nearest start date.
     * The caller decides what business concept to attach (e.g. "special" = next 7 days).
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, NULL AS categoryName
            FROM events e
            JOIN event_sessions es ON e.id = es.event_id
            JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
              AND es.start_date BETWEEN :from AND :to
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY MIN(es.start_date) ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<EventWithCategoryDto> findEventsStartingBetween(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to,
            @Param("limit") int limit);

    /**
     * Randomly sampled PLANNED/ONGOING events.
     * Reusable for any section needing a random selection — the caller sets the count
     * (e.g. "trending" = findRandomEvents(3), "suggested" = findRandomEvents(10)).
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, NULL AS categoryName
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY RANDOM()
            LIMIT :limit
            """, nativeQuery = true)
    List<EventWithCategoryDto> findRandomEvents(@Param("limit") int limit);

    // ─── Homepage: Event Display with Filters ─────────────────────────────────

    /**
     * Filtered event list without a category restriction.
     * Supports optional date-range filter and a free-events-only toggle.
     * Used by: homepage feature
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, c.name AS categoryName
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            LEFT JOIN categories c ON e.category_id = c.id
            WHERE e.status IN ('PLANNED', 'ONGOING')
              AND (CAST(:startOdt AS TIMESTAMPTZ) IS NULL OR es.start_date >= :startOdt)
              AND (CAST(:endOdt AS TIMESTAMPTZ) IS NULL OR es.start_date < :endOdt)
              AND (:isFreeOnly IS FALSE OR EXISTS (
                  SELECT 1 FROM event_sessions es2
                  JOIN zones z2 ON es2.id = z2.session_id
                  WHERE es2.event_id = e.id AND (z2.price = 0 OR z2.price IS NULL)
              ))
            GROUP BY e.id, e.name, e.banner_url, e.address_line, c.name
            ORDER BY MIN(es.start_date) ASC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<EventWithCategoryDto> findEventsWithoutCategoryFilter(
            @Param("startOdt") OffsetDateTime startOdt,
            @Param("endOdt") OffsetDateTime endOdt,
            @Param("isFreeOnly") Boolean isFreeOnly,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);

    /**
     * Same as {@link #findEventsWithoutCategoryFilter} but restricted to the given category IDs.
     * Used by: homepage feature
     */
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice, c.name AS categoryName
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            LEFT JOIN categories c ON e.category_id = c.id
            WHERE e.status IN ('PLANNED', 'ONGOING')
              AND e.category_id IN (:categoryIds)
              AND (CAST(:startOdt AS TIMESTAMPTZ) IS NULL OR es.start_date >= :startOdt)
              AND (CAST(:endOdt AS TIMESTAMPTZ) IS NULL OR es.start_date < :endOdt)
              AND (:isFreeOnly IS FALSE OR EXISTS (
                  SELECT 1 FROM event_sessions es2
                  JOIN zones z2 ON es2.id = z2.session_id
                  WHERE es2.event_id = e.id AND (z2.price = 0 OR z2.price IS NULL)
              ))
            GROUP BY e.id, e.name, e.banner_url, e.address_line, c.name
            ORDER BY MIN(es.start_date) ASC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<EventWithCategoryDto> findEventsWithCategoryFilter(
            @Param("startOdt") OffsetDateTime startOdt,
            @Param("endOdt") OffsetDateTime endOdt,
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("isFreeOnly") Boolean isFreeOnly,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);
}
