package com.ticket4u.mapper;

import com.ticket4u.dto.auth.GoogleUserInfo;
import com.ticket4u.dto.auth.RegisterRequest;
import com.ticket4u.dto.auth.RegisterResponse;
import com.ticket4u.dto.user.UserCreateRequest;
import com.ticket4u.dto.user.UserResponse;
import com.ticket4u.dto.user.UserUpdateRequest;
import com.ticket4u.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {PasswordEncoderMapper.class, UserHelperMapper.class}
)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoderMapper passwordEncoderMapper;

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "role", source = "role.roleName")
    public abstract UserResponse toDTO(User user);

    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract User toEntity(UserCreateRequest userCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract void updateUserFromDTO(UserUpdateRequest dto, @MappingTarget User entity);

    // Register methods
    @Mapping(target = "email", expression = "java(request.email().toLowerCase())")
    @Mapping(target = "username", source = "email", qualifiedByName = "extractUsername")
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "normalizePhone")
    @Mapping(target = "firstName", source = "fullName", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "fullName", qualifiedByName = "extractLastName")
    @Mapping(target = "isActive", constant = "false")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract User toEntityFromRegister(RegisterRequest request);

    @Mapping(target = "email", expression = "java(userInfo.email().toLowerCase())")
    @Mapping(target = "username", source = "email", qualifiedByName = "extractUsername")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "firstName", source = "name", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "name", qualifiedByName = "extractLastName")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    protected abstract User toEntityFromGoogleBase(GoogleUserInfo userInfo);

    public User toEntityFromGoogle(GoogleUserInfo userInfo) {
        User user = toEntityFromGoogleBase(userInfo);
        user.setPasswordHash(passwordEncoderMapper.generateRandomHashedPassword());
        return user;
    }

    public RegisterResponse toRegisterResponse(User user, String message) {
        return new RegisterResponse(user.getId(), user.getEmail(), message);
    }
}
