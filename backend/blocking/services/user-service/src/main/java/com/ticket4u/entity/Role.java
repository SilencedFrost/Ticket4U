package com.ticket4u.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "role", schema = "public")
@NoArgsConstructor
public class Role {

    @Id
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}
