package com.ticket4u.core.repository;

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
}
