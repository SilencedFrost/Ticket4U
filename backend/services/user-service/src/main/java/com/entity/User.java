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
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Setter
    @Column(nullable = false, length = 64)
    private String username;

    @Setter
    @Column(length = 32)
    private String firstName;

    @Setter
    @Column(length = 32)
    private String lastName;

    @Setter
    @Column
    private LocalDate birthday;

    @Setter
    @Column(nullable = false)
    private String passwordHash;

    @Setter
    @Column(nullable = false)
    private Boolean isActive = false;

    @Setter
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Setter
    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(nullable = false)
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