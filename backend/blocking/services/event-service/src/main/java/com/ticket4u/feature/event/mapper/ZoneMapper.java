package com.ticket4u.feature.event.mapper;

import com.ticket4u.core.entity.Zone;
import com.ticket4u.feature.event.dto.ZoneResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    @Mapping(target = "available", expression = "java(zone.getCapacity() - zone.getQuantitySold())")
    ZoneResponse toSeatTypeResponse(Zone zone);
}
