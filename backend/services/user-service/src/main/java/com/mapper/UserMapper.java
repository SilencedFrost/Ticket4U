package com.mapper;

import com.dto.auth.RegisterRequest;
import com.dto.user.UserCreateRequest;
import com.dto.user.UserResponse;
import com.dto.user.UserUpdateRequest;
import com.entity.User;
import com.service.HashService;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roleId", source = "role.roleId")
    UserResponse toDTO(User user);

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "carts", ignore = true)
    User toEntity(UserCreateRequest userCreateRequest, @Context HashService hashService);

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "carts", ignore = true)
    User toEntity(RegisterRequest registerRequest, @Context HashService hashService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "carts", ignore = true)
    void updateUserFromDTO(UserUpdateRequest dto, @MappingTarget User entity, @Context HashService hashService);

    @Named("hashPassword")
    default String hashPassword(String password, @Context HashService hashService) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return hashService.hashPassword(password);
    }
}
