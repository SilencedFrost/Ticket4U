package com.ticket4u.feature.homepage.repository;

import com.ticket4u.core.Event;
import com.ticket4u.feature.homepage.projection.EventSummaryProjection;
import java.time.OffsetDateTime;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    // Lấy danh sách events với giá thấp nhất từ zones qua event_sessions
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY MIN(es.start_date) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<EventSummaryProjection> findEventsWithMinPrice(
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);

    // Lấy giá thấp nhất của 1 event qua event_sessions
    @Query(value = """
            SELECT MIN(z.price)
            FROM event_sessions es
            JOIN zones z ON es.id = z.session_id
            WHERE es.event_id = :eventId
            """, nativeQuery = true)
    Double findMinPriceByEventId(@Param("eventId") UUID eventId);

    // Featured: Events mới nhất (PLANNED/ONGOING)
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY e.created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<EventSummaryProjection> findFeaturedEvents();

    // Special: Events sắp diễn ra trong 7 ngày
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM events e
            JOIN event_sessions es ON e.id = es.event_id
            JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
              AND es.start_date BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + INTERVAL '7 days'
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY MIN(es.start_date) ASC
            LIMIT 10
            """, nativeQuery = true)
    List<EventSummaryProjection> findSpecialEvents();

    // Trending: Random 3 PLANNED/ONGOING events
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY RANDOM()
            LIMIT 3
            """, nativeQuery = true)
    List<EventSummaryProjection> findTrendingEvents();

    // Suggested: Random PLANNED/ONGOING events
    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM events e
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
            WHERE e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line
            ORDER BY RANDOM()
            LIMIT 10
            """, nativeQuery = true)
    List<EventSummaryProjection> findSuggestedEvents();

    // Event Display: Filter events WITHOUT category filter
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
    List<EventWithCategoryProjection> findEventsWithoutCategoryFilter(
            @Param("startOdt") OffsetDateTime startOdt,
            @Param("endOdt") OffsetDateTime endOdt,
            @Param("isFreeOnly") Boolean isFreeOnly,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);

    // Event Display: Filter events WITH category filter
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
    List<EventWithCategoryProjection> findEventsWithCategoryFilter(
            @Param("startOdt") OffsetDateTime startOdt,
            @Param("endOdt") OffsetDateTime endOdt,
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("isFreeOnly") Boolean isFreeOnly,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset);
}
