package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CategoryMapper {
    CategoryResponse toDTO(Category category);
    CategorySummaryResponse toSummaryDTO(Category category);
}
