package com.ticket4u.mapper;

import com.ticket4u.dto.auth.GoogleUserInfo;
import com.ticket4u.dto.auth.RegisterRequest;
import com.ticket4u.dto.auth.RegisterResponse;
import com.ticket4u.dto.user.*;
import com.ticket4u.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {PasswordEncoderMapper.class, UserHelperMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoderMapper passwordEncoderMapper;

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "role", source = "role.roleName")
    public abstract UserResponse toDTO(User user);

    // Register methods
    @Mapping(target = "username", source = "email", qualifiedByName = "extractUsername")
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "normalizePhone")
    @Mapping(target = "firstName", source = "fullName", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "fullName", qualifiedByName = "extractLastName")
    @Mapping(target = "isActive", constant = "false")
    @Mapping(target = "isDeleted", constant = "false")
    public abstract User toEntityFromRegister(RegisterRequest request);

    // Base method, does not get used elsewhere
    @Mapping(target = "username", source = "email", qualifiedByName = "extractUsername")
    @Mapping(target = "firstName", source = "name", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "name", qualifiedByName = "extractLastName")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isDeleted", constant = "false")
    protected abstract User toEntityFromGoogleBase(GoogleUserInfo userInfo);

    // Actual method, generates random password
    public User toEntityFromGoogle(GoogleUserInfo userInfo) {
        User user = toEntityFromGoogleBase(userInfo);
        user.setPasswordHash(passwordEncoderMapper.generateRandomHashedPassword());
        return user;
    }

    public RegisterResponse toRegisterResponse(User user, String message) {
        return new RegisterResponse(user.getId(), user.getEmail(), message);
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "normalizePhone")
    public abstract void updateEntity(ChangeInfoRequest request, @MappingTarget User entity);

    public abstract UserSummaryResponse toSummaryResponse(User user);
}
