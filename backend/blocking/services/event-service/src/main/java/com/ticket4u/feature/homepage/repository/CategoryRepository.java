package com.ticket4u.feature.homepage.repository;

import com.ticket4u.core.Category;
import com.ticket4u.feature.homepage.projection.CategoryProjection;
import com.ticket4u.feature.homepage.projection.CategoryWithEventProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    // Get all categories that have at least one PLANNED or ONGOING event
    @Query(value = """
        SELECT DISTINCT c.id AS id, c.name AS name
        FROM categories c
        LEFT JOIN events e ON c.id = e.category_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
        ORDER BY c.id
        """, nativeQuery = true)
    List<CategoryProjection> findCategoriesWithActiveEvents();

    // Get all categories with their latest 4 events in a SINGLE query (avoids N+1)
    @Query(value = """
        SELECT c.id AS categoryId, c.name AS categoryName,
               e.id AS eventId, e.name AS eventName, e.banner_url AS bannerUrl,
               e.address_line AS addressLine, e.start_date AS startDate, e.end_date AS endDate,
               MIN(z.price) AS minPrice
        FROM categories c
        JOIN events e ON c.id = e.category_id
        LEFT JOIN zones z ON e.id = z.event_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
          AND e.id IN (
              SELECT sub.id FROM (
                  SELECT ev.id, ev.category_id,
                         ROW_NUMBER() OVER (PARTITION BY ev.category_id ORDER BY ev.created_at DESC) AS rn
                  FROM events ev
                  WHERE ev.status IN ('PLANNED', 'ONGOING')
              ) sub
              WHERE sub.rn <= 4
          )
        GROUP BY c.id, c.name, e.id, e.name, e.banner_url, e.address_line, e.start_date, e.end_date
        ORDER BY c.id, e.created_at DESC
        """, nativeQuery = true)
    List<CategoryWithEventProjection> findCategoriesWithLatestEvents();
}
