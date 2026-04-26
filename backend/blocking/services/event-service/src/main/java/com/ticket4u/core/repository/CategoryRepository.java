package com.ticket4u.core.repository;

import com.ticket4u.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c.id FROM Category c")
    List<Integer> findAllCategoryIds();
}
