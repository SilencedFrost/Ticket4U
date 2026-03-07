package com.ticket4u.core.repository;

import com.ticket4u.core.dto.CategoryWithEventDto;
import com.ticket4u.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for the Category aggregate root.
 * Lives in core because Category is a shared domain entity, not owned by any single feature.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Returns all categories that have at least one active (PLANNED or ONGOING) event.
     * Used for populating the category filter chips on the homepage.
     * Used by: homepage feature
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.events e WHERE e.status IN ('PLANNED', 'ONGOING') ORDER BY c.id")
    List<Category> findCategoriesWithActiveEvents();

    /**
     * Returns all categories with their latest 4 active events as flat (category + event) rows.
     * The service layer groups the flat result by categoryId into a structured response.
     * Used by: homepage feature — "Browse by Category" section
     */
    @Query(value = """
            SELECT c.id AS categoryId, c.name AS categoryName,
                   e.id AS eventId, e.name AS eventName, e.banner_url AS bannerUrl,
                   e.address_line AS addressLine,
                   MIN(es.start_date) AS startDate, MAX(es.end_date) AS endDate,
                   MIN(z.price) AS minPrice
            FROM categories c
            JOIN events e ON c.id = e.category_id
            LEFT JOIN event_sessions es ON e.id = es.event_id
            LEFT JOIN zones z ON es.id = z.session_id
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
            GROUP BY c.id, c.name, e.id, e.name, e.banner_url, e.address_line
            ORDER BY c.id, e.created_at DESC
            """, nativeQuery = true)
    List<CategoryWithEventDto> findCategoriesWithLatestEvents();
}
