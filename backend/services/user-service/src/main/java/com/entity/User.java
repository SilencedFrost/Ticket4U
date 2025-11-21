package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users", schema = "public")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

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
    @Column(name = "password_hash", nullable = false, length = 60, columnDefinition = "char(60)")
    private String passwordHash;

    @Setter
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default false")
    private Boolean isActive = false;

    @Setter
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private OffsetDateTime creationDate;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private final List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private final List<Cart> carts = new ArrayList<>();

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