package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users", schema = "public")
@NoArgsConstructor
public class User {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Setter
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Setter
    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @Setter
    @Column(name = "first_name", length = 32)
    private String firstName;

    @Setter
    @Column(name = "last_name", length = 32)
    private String lastName;

    @Setter
    @Column(name = "birthday")
    private LocalDate birthday;

    @Setter
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Setter
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean")
    private Boolean isActive = false;

    @Setter
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean")
    private Boolean isDeleted = false;

    @Setter
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private final List<Session> sessions = new ArrayList<>();

    public void assignRole(Role role) {
        if(this.role != null) {
            this.role.getUsers().remove(this);
        }

        this.role = role;
        if(role != null) {
            role.getUsers().add(this);
        }
    }
}