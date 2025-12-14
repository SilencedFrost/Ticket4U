package com.ticket4u.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "role", schema = "public")
@NoArgsConstructor
public class Role {

    @Id
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
