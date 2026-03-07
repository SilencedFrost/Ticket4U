package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.EventSession;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public abstract class EventDetailMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "sessions", target = "showtimes")

    @Mapping(target = "minPrice", expression = "java(calculateMinPrice(event))")
    @Mapping(target = "maxPrice", expression = "java(calculateMaxPrice(event))")
    @Mapping(target = "startDate", expression = "java(calculateStartDate(event))")
    public abstract EventDetailResponse toResponse(Event event);

    protected BigDecimal calculateMinPrice(Event event) {
        return event.getSessions().stream()
                .flatMap(s -> s.getZones().stream())
                .map(Zone::getPrice)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    protected BigDecimal calculateMaxPrice(Event event) {
        return event.getSessions().stream()
                .flatMap(s -> s.getZones().stream())
                .map(Zone::getPrice)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    protected OffsetDateTime calculateStartDate(Event event) {
        return event.getSessions().stream()
                .map(EventSession::getStartDate)
                .min(OffsetDateTime::compareTo).orElse(null);
    }

    public abstract ShowtimeResponse toShowtimeResponse(EventSession session);
}
