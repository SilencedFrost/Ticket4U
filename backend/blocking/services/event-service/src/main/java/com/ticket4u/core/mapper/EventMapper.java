package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.EventResponse;
import com.ticket4u.core.dto.EventSummaryResponse;
import com.ticket4u.core.entity.Event;
import com.ticket4u.core.entity.EventSession;
import com.ticket4u.core.entity.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", uses = SessionMapper.class)
public abstract class EventMapper {

    /**
     * Use "Event.withAllEntities" entity graph
     * @param event The input entity
     * @return EventSummaryResponse
     */
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    // Mappings that require helper method
    @Mapping(target = "startDate", source = "event", qualifiedByName = "toStartDate")
    @Mapping(target = "endDate", source = "event", qualifiedByName = "toEndDate")
    @Mapping(target = "minPrice", source = "event", qualifiedByName = "toMinPrice")
    public abstract EventSummaryResponse toSummaryDTO(Event event);

    /**
     * Use "Event.withAllEntities" entity graph
     * @param event The input entity
     * @return EventSummaryResponse
     */
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "categoryName", source = "category.name")
    // Mappings that require helper method
    @Mapping(target = "startDate", source = "event", qualifiedByName = "toStartDate")
    @Mapping(target = "endDate", source = "event", qualifiedByName = "toEndDate")
    @Mapping(target = "minPrice", source = "event", qualifiedByName = "toMinPrice")
    @Mapping(target = "maxPrice", source = "event", qualifiedByName = "toMaxPrice")
    public abstract EventResponse toDTO(Event event);

    @Named("toMinPrice")
    protected BigDecimal calculateMinPrice(Event event) {
        return event.getSessions().stream()
                .flatMap(s -> s.getZones().stream())
                .map(Zone::getPrice)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    @Named("toMaxPrice")
    protected BigDecimal calculateMaxPrice(Event event) {
        return event.getSessions().stream()
                .flatMap(s -> s.getZones().stream())
                .map(Zone::getPrice)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    @Named("toStartDate")
    protected OffsetDateTime calculateStartDate(Event event) {
        return event.getSessions().stream()
                .map(EventSession::getStartDate)
                .min(OffsetDateTime::compareTo).orElse(null);
    }

    @Named("toEndDate")
    protected OffsetDateTime calculateEndDate(Event event) {
        return event.getSessions().stream()
                .map(EventSession::getEndDate)
                .max(OffsetDateTime::compareTo).orElse(null);
    }
}
