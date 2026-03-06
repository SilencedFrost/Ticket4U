package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    List<SeatTypeResponse> toSeatTypeResponse(Collection<Zone> zones);

    @Mapping(target = "available", expression = "java(zone.getCapacity() - zone.getQuantitySold())")
    SeatTypeResponse toSeatTypeResponse(Zone zone);
}
