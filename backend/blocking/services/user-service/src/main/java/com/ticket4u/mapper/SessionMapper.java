package com.ticket4u.mapper;

import com.ticket4u.dto.session.SessionResponse;
import com.ticket4u.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = SessionHelperMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SessionMapper {

    @Mapping(target = "displayId", source = "id", qualifiedByName = "toDisplayId")
    @Mapping(target = "userAgent", source = "userAgent", qualifiedByName = "normalizeUserAgent")
    SessionResponse toResponse(Session session);

    List<SessionResponse> toResponseList(List<Session> sessions);
}
