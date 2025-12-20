package com.ticket4u.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @Named("hashPassword")
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Named("generateRandomHashedPassword")
    public String generateRandomHashedPassword() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }
}
