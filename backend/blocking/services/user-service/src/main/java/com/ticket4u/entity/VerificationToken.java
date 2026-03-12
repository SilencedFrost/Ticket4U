package com.ticket4u.entity;

import com.ticket4u.constant.TokenType;
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
@Table(
        name = "verification_token",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"token_hash", "token_type"})
)
@NoArgsConstructor
public class VerificationToken {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 64, columnDefinition = "char(64)")
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TokenType tokenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public VerificationToken(String tokenHash, TokenType tokenType, User user, OffsetDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.tokenType = tokenType;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(expiresAt);
    }
}
