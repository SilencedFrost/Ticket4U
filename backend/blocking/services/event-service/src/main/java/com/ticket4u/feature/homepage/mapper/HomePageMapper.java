package com.ticket4u.feature.homepage.mapper;

import com.ticket4u.core.Category;
import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.homepage.dto.CategoryDTO;
import com.ticket4u.feature.homepage.dto.EventCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for HomePage feature
 * Converts Entity objects to DTOs using MapStruct
 * For native query results (Object[]), use NativeQueryMapper instead
 */
@Mapper(
        componentModel = "spring",
        uses = {HomePageHelperMapper.class}
)
public interface HomePageMapper {
    /**
     * Map Category entity to CategoryDTO
     * @param category Category entity from database
     * @param eventCount Number of events in this category (calculated separately)
     * @return CategoryDTO for frontend display
     */
    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "eventCount", source = "eventCount")
    CategoryDTO toCategoryDTO(Category category, Integer eventCount);
}
