package com.ticket4u.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "organizer", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Organizer {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String logo_url;

    @Column(precision = 3)
    private Double rating;
}
