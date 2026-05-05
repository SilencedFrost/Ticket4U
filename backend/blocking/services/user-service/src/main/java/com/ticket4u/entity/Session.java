package com.ticket4u.entity;

import com.ticket4u.constant.TokenConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "session", schema = "public")
@NoArgsConstructor
public class Session {

    public Session(User user, String sessionHash, String userAgent, Boolean persistent) {
        this.assignUser(user);
        this.sessionHash = sessionHash;
        this.userAgent = userAgent;
        this.persistent = persistent;
        this.expiresAt = OffsetDateTime.now().plus(persistent? TokenConstants.REFRESH_TOKEN.getRollingTTL() : Duration.ofDays(1));
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    private Boolean persistent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 64, columnDefinition = "char(64)")
    private String sessionHash;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Column(columnDefinition = "text")
    private String userAgent;

    public void assignUser(User user) {
        if(this.user != null) {
            this.user.getSessions().remove(this);
        }

        this.user = user;
        if(user != null) {
            user.getSessions().add(this);
        }
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = OffsetDateTime.now();
        }
    }
}