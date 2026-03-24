package com.ticket4u.entity;

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
        super(emailAsUsername, passwordHash, true, true, true, true, authorities);
        this.userId = userId;
        this.roleId = roleId;
        this.trueUsername = trueUsername;
    }

    public CustomUserDetails(String emailAsUsername, String passwordHash, boolean enabled, Collection<? extends GrantedAuthority> authorities, UUID userId, int roleId, String trueUsername) {
        super(emailAsUsername, passwordHash, enabled, true, true, true, authorities);
        this.userId = userId;
        this.roleId = roleId;
        this.trueUsername = trueUsername;
    }

    // For JWT creation
    public CustomUserDetails(Collection<? extends  GrantedAuthority> authorities, UUID userId) {
        super("jwt-user", null, authorities);
        this.userId = userId;
        this.roleId = -1;
        this.trueUsername = "jwt-user";
    }
}
