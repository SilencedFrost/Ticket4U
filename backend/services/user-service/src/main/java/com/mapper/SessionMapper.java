package com.mapper;

import com.dto.session.SessionCreateRequest;
import com.dto.session.SessionResponse;
import com.entity.Session;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "isActive", ignore = true)
    SessionResponse toDTO(Session session);

    @Mapping(target = "sessionHash", expression = "java(hashToken(sessionCreateRequest.sessionToken()))")
    @Mapping(target = "lastAccessed", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    @Mapping(target = "revokeReason", ignore = true)
    Session toEntity(SessionCreateRequest sessionCreateRequest);

    default String hashToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("sessionToken cannot be null");
        }
        return DigestUtils.sha256Hex(token);
    }
}
