package com.mapper;

import com.dto.user.UserCreateRequest;
import com.dto.user.UserResponse;
import com.dto.user.UserUpdateRequest;
import com.entity.User;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = PasswordEncoderMapper.class
)
public interface UserMapper {

    @Mapping(target = "roleId", source = "role.id")
    UserResponse toDTO(User user);

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    User toEntity(UserCreateRequest userCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    void updateUserFromDTO(UserUpdateRequest dto, @MappingTarget User entity);
}
