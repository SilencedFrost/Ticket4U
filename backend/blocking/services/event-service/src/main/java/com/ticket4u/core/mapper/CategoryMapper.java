package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.entity.Category;
import com.ticket4u.core.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CategoryMapper {
    CategoryResponse toDTO(Category category);

    @Mapping(target = "events", source = "limitedEvents")
    CategoryResponse toDTO(Category category, List<Event> limitedEvents);

    CategorySummaryResponse toSummaryDTO(Category category);
}
