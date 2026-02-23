package com.ticket4u.feature.homepage.repository;

import com.ticket4u.core.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    
    // Lấy danh sách events với giá thấp nhất từ các zones
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date, 
               MIN(z.price) as min_price
        FROM events e 
        LEFT JOIN zones z ON e.id = z.event_id 
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY e.start_date DESC
        """, nativeQuery = true)
    List<Object[]> findEventsWithMinPrice();
    
    // Hoặc nếu muốn lấy theo event_id cụ thể
    @Query(value = """
        SELECT MIN(z.price)
        FROM zones z
        WHERE z.event_id = :eventId
        """, nativeQuery = true)
    Double findMinPriceByEventId(@Param("eventId") UUID eventId);

    // Featured: Events mới nhất (PLANNED/ONGOING)
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY e.created_at DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findFeaturedEvents();

    // Special: Events sắp diễn ra trong 7 ngày
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND e.start_date BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + INTERVAL '7 days'
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY e.start_date ASC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findSpecialEvents();

    // Trending: Random 3 PLANNED/ONGOING events
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY RANDOM()
        LIMIT 3
        """, nativeQuery = true)
    List<Object[]> findTrendingEvents();

    // Suggested: Random PLANNED/ONGOING events
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY RANDOM()
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findSuggestedEvents();

    // Music: Events với category = 'Music'
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        JOIN categories c ON e.category_id = c.id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND c.name = :categoryName
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY e.start_date DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findEventsByCategory(@Param("categoryName") String categoryName);

    // Get latest 4 events for a specific category ID
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND e.category_id = :categoryId
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY e.created_at DESC
        LIMIT 4
        """, nativeQuery = true)
    List<Object[]> findLatestEventsByCategoryId(@Param("categoryId") Integer categoryId);

    // Event Display: Filter events WITHOUT category filter (when showing all categories)
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price, c.name as category_name
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        LEFT JOIN categories c ON e.category_id = c.id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND (:startDate IS NULL OR e.start_date >= TO_TIMESTAMP(:startDate, 'YYYY-MM-DD'))
          AND (:endDate IS NULL OR e.start_date <= TO_TIMESTAMP(:endDate, 'YYYY-MM-DD') + INTERVAL '1 day')
          AND (:isFreeOnly IS FALSE OR EXISTS (
              SELECT 1 FROM zones z2 
              WHERE z2.event_id = e.id AND (z2.price = 0 OR z2.price IS NULL)
          ))
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date, c.name
        ORDER BY e.start_date ASC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> findEventsWithoutCategoryFilter(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("isFreeOnly") Boolean isFreeOnly,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

    // Event Display: Filter events WITH category filter (when specific categories selected)
    @Query(value = """
        SELECT e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date,
               MIN(z.price) as min_price, c.name as category_name
        FROM events e
        LEFT JOIN zones z ON e.id = z.event_id
        LEFT JOIN categories c ON e.category_id = c.id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND e.category_id IN (:categoryIds)
          AND (:startDate IS NULL OR e.start_date >= TO_TIMESTAMP(:startDate, 'YYYY-MM-DD'))
          AND (:endDate IS NULL OR e.start_date <= TO_TIMESTAMP(:endDate, 'YYYY-MM-DD') + INTERVAL '1 day')
          AND (:isFreeOnly IS FALSE OR EXISTS (
              SELECT 1 FROM zones z2 
              WHERE z2.event_id = e.id AND (z2.price = 0 OR z2.price IS NULL)
          ))
        GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date, c.name
        ORDER BY e.start_date ASC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> findEventsWithCategoryFilter(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("categoryIds") List<Integer> categoryIds,
        @Param("isFreeOnly") Boolean isFreeOnly,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );
}
