package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.entity.Event;
import com.ticket4u.core.entity.EventSession;
import com.ticket4u.core.entity.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public abstract class EventDetailMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "sessions", target = "showtimes")

    @Mapping(target = "minPrice", source = "event", qualifiedByName = "toMinPrice")
    @Mapping(target = "maxPrice", source = "event", qualifiedByName = "toMaxPrice")
    @Mapping(target = "startDate", source = "event", qualifiedByName = "toStartDate")
    public abstract EventDetailResponse toResponse(Event event);

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

    public abstract ShowtimeResponse toShowtimeResponse(EventSession session);
}
