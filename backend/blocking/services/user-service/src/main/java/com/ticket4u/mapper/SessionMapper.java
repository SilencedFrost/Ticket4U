package com.ticket4u.mapper;

import com.ticket4u.dto.session.SessionResponse;
import com.ticket4u.entity.Session;
import com.ticket4u.util.HashUtil;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = HashUtil.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SessionMapper {

    @Mapping(target = "displayId", source = "id", qualifiedByName = "toHashId")
    @Mapping(target = "isCurrent", expression = "java(isCurrent)")
    SessionResponse toDTO(Session session, @Context boolean isCurrent);

}
