package com.ticket4u.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "event_sessions", schema = "public")
public class EventSession {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SessionStatus status;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "session")
    private Set<Zone> zones = new HashSet<>();

    public enum SessionStatus {
        EDITING,
        PREMIERE,
        SELLING,
        PAUSED,
        ONGOING,
        FINISHED,
        CANCELLED
    }
}
