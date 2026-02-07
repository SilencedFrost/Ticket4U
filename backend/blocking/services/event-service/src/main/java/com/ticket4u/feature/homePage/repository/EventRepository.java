package com.ticket4u.feature.homePage.repository;

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
        JOIN zones z ON e.id = z.event_id
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
}
