package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(name = "session", schema = "public")
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Setter
    @Column(name = "session_hash", nullable = false, updatable = false, length = 64, columnDefinition = "char(64)")
    private String sessionHash;

    @Setter
    @Column(name = "last_accessed", nullable = false)
    private OffsetDateTime lastAccessed;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Setter
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Setter
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Setter
    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    @Setter
    @Column(name = "revoke_reason", length = 128)
    private String revokeReason;

    @Setter
    @Column(name = "user_agent", nullable = false, columnDefinition = "text")
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
        if (expiresAt == null) {
            expiresAt = createdAt.plusDays(7);
        }
        if (lastAccessed == null) {
            lastAccessed = OffsetDateTime.now();
        }
    }
}