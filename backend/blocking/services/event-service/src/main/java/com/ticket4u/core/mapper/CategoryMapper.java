package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.CategoryResponse;
import com.ticket4u.core.dto.CategorySummaryResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.entity.Category;
import com.ticket4u.core.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Lazy
    @Autowired
    protected EventMapper eventMapper;

    public abstract CategoryResponse toDTO(Category category);

    @Mapping(target = "events", source = "limitedEvents")
    public abstract CategoryResponse toDTO(Category category, List<Event> limitedEvents);

    public abstract CategorySummaryResponse toSummaryDTO(Category category);

    /**
     * Bridge method for MapStruct to convert Event to EventSummaryResponse.
     * Delegates to EventMapper, loaded lazily to break the circular dependency.
     */
    protected EventSummaryResponse eventToSummaryDTO(Event event) {
        return eventMapper.toSummaryDTO(event);
    }
}
