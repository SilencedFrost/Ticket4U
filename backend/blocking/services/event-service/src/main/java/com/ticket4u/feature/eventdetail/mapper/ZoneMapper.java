package com.ticket4u.feature.eventdetail.mapper;

import com.ticket4u.core.Zone;
import com.ticket4u.feature.eventdetail.dto.SeatTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    List<SeatTypeResponse> toSeatTypeResponse(List<Zone> zones);

    @Mapping(source = "price", target = "price")
    @Mapping(target = "available", expression = "java(zone.getCapacity() - zone.getQuantitySold())")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "giftImageUrl", target = "image")
    @Mapping(source = "perks", target = "benefits")
    SeatTypeResponse toSeatTypeResponse(Zone zone);
}
