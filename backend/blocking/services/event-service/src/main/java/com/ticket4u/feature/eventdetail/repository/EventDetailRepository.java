package com.ticket4u.feature.eventdetail.repository;

import com.ticket4u.core.Event;
import com.ticket4u.feature.homepage.projection.EventWithCategoryProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventDetailRepository extends JpaRepository<Event, UUID> {
    @EntityGraph(attributePaths = { "category", "zones" })
    Optional<Event> findWithDetailsById(UUID id);

    @Query(value = """
            SELECT e.id AS id, e.name AS name, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine, e.start_date AS startDate, e.end_date AS endDate,
                   MIN(z.price) AS minPrice,
                   c.name AS categoryName
            FROM events e
            LEFT JOIN zones z ON e.id = z.event_id
            LEFT JOIN categories c ON e.category_id = c.id
            WHERE e.id != :currentId
              AND e.status IN ('PLANNED', 'ONGOING')
            GROUP BY e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date, c.name
            ORDER BY
                (CASE WHEN e.category_id = :categoryId THEN 1 ELSE 0 END) DESC,
                (CASE WHEN POSITION(LOWER(:city) IN LOWER(e.address_line)) > 0 THEN 1 ELSE 0 END) DESC,
                e.start_date ASC,
                e.created_at DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<EventWithCategoryProjection> findRelatedEvents(
            @Param("currentId") UUID currentId,
            @Param("categoryId") Integer categoryId,
            @Param("city") String city,
            @Param("limit") Integer limit);
}
