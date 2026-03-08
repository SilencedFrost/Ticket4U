package com.ticket4u.core.mapper;

import com.ticket4u.core.entity.Zone;
import com.ticket4u.core.dto.ZoneResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    @Mapping(target = "available", expression = "java(zone.getCapacity() - zone.getQuantitySold())")
    ZoneResponse toDTO(Zone zone);
}
