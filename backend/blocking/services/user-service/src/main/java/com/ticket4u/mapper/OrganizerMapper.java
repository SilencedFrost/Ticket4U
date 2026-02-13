package com.ticket4u.mapper;

import com.ticket4u.dto.organizer.OrganizerDTO;
import com.ticket4u.entity.Organizer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    @Mapping(source = "logo_url", target = "avatar")
    OrganizerDTO toDTO(Organizer organizer);
}
