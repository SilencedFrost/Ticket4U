package com.ticket4u.feature.homepage.repository;

import com.ticket4u.core.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    // Get all categories that have at least one PLANNED or ONGOING event
    @Query(value = """
        SELECT DISTINCT c.id, c.name
        FROM categories c
        LEFT JOIN events e ON c.id = e.category_id
        WHERE e.status IN ('PLANNED', 'ONGOING')
        ORDER BY c.id
        """, nativeQuery = true)
    List<Object[]> findCategoriesWithActiveEvents();
}
