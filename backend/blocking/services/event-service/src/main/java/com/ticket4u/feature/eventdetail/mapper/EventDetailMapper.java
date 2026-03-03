package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Event;
import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.EventDetailResponse;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import com.ticket4u.feature.eventdetail.dto.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public abstract class EventDetailMapper {

    @Autowired
    protected ZoneMapper zoneMapper;

    @Mapping(source = "event.id", target = "id")
    @Mapping(source = "event.name", target = "name")
    @Mapping(source = "event.description", target = "description")
    @Mapping(source = "event.seatingPlanImageUrl", target = "seatingPlanImageUrl")
    @Mapping(source = "event.category.id", target = "categoryId")
    @Mapping(source = "event.zones", target = "showtimes", qualifiedByName = "mapZonesToShowtimes")
    public abstract EventDetailResponse toResponse(
            Event event,
            String minPrice,
            String maxPrice
    );

    @Named("mapZonesToShowtimes")
    protected List<ShowtimeResponse> mapZonesToShowtimes(List<Zone> zones) {
        if (zones == null || zones.isEmpty()) return Collections.emptyList();

        List<SeatTypeResponse> seatTypes = zoneMapper.toSeatTypeResponse(zones);

        Event event = zones.get(0).getEvent();

        return List.of(new ShowtimeResponse(
                event.getId().toString(),
                event.getStartDate(),
                seatTypes
        ));
    }
}
