package com.ticket4u.core.mapper;

import com.ticket4u.core.dto.EventSessionResponse;
import com.ticket4u.core.entity.EventSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    EventSessionResponse toDTO(EventSession eventSession);
}
