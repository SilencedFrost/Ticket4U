package com.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class CustomUserDetails extends User {
    private final UUID userId;
    private final int roleId;
    private final String trueUsername;

    public CustomUserDetails(String emailAsUsername, String passwordHash, Collection<? extends GrantedAuthority> authorities, UUID userId, int roleId, String trueUsername) {
        super(emailAsUsername, passwordHash, authorities);
        this.userId = userId;
        this.roleId = roleId;
        this.trueUsername = trueUsername;
    }
}
