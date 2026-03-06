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
@Setter
@Entity
@Table(name = "email_verification_token", schema = "public")
@NoArgsConstructor
public class EmailVerificationToken {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64, columnDefinition = "char(64)")
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public EmailVerificationToken(String tokenHash, User user, OffsetDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(expiresAt);
    }
}
