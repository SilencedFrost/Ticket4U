package com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "role", schema = "public")
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "role_id", updatable = false, nullable = false)
    private Integer roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
