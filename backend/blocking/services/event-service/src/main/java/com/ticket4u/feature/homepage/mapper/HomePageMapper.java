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
     * Map Event entity to EventCardDTO for homepage event cards
     * @param event Event entity from database
     * @param zones List of zones belonging to this event (for price calculation)
     * @return EventCardDTO for frontend display
     */
    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "name", source = "event.name")
    @Mapping(target = "bannerUrl", source = "event.bannerUrl")
    @Mapping(target = "startDate", source = "event.startDate")
    @Mapping(target = "endDate", source = "event.endDate")
    @Mapping(target = "addressLine", source = "event.addressLine")
    @Mapping(target = "categoryName", expression = "java(event.getCategory() != null ? event.getCategory().getName() : null)")
    @Mapping(target = "status", expression = "java(event.getStatus() != null ? event.getStatus().name() : null)")
    @Mapping(target = "minPrice", source = "zones", qualifiedByName = "computeMinPrice")
    EventCardDTO toEventWithMinPriceDTO(Event event, List<Zone> zones);

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
