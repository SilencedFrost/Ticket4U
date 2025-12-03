package com.ticket4u.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "session", schema = "public")
@NoArgsConstructor
public class Session {

    public Session(User user, String sessionHash, String userAgent, OffsetDateTime expiryDate) {
        this.assignUser(user);
        this.sessionHash = sessionHash;
        this.userAgent = userAgent;
        this.expiresAt = expiryDate;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Setter
    @Column(nullable = false, updatable = false, length = 64, columnDefinition = "char(64)")
    private String sessionHash;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Setter
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