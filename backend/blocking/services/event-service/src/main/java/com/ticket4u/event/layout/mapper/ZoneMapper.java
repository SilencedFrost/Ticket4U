package com.ticket4u.event.layout.mapper;

import com.ticket4u.core.dto.ZoneResponse;
import com.ticket4u.core.entity.Zone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SeatMapper.class)
public interface ZoneMapper {

    ZoneResponse toDTO(Zone zone);
}
