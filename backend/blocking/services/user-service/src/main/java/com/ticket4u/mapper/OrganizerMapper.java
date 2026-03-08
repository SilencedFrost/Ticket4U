package com.ticket4u.mapper;

import com.ticket4u.dto.organizer.OrganizerDTO;
import com.ticket4u.entity.Organizer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    OrganizerDTO toDTO(Organizer organizer);
}
