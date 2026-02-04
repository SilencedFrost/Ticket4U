package com.ticket4u.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 254)
    private String name;

    @Column(name = "organizer_id")
    private UUID organizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 254)
    private String addressLine;

    @CreationTimestamp //auto create time
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startDate;

    @CreationTimestamp //auto create time
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(name = "end_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endDate;

    @Column(nullable = false, length = 48)
    private EventStatus status;

    @Column(columnDefinition = "TEXT")
    private String bannerUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "create_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updateAt;

    @Column(name = "cancelled_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime cancelledAt;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private EventContent content;

    public enum EventStatus {
        PLANNED,
        ONGOING,
        FINISHED,
        CANCELLED
    }
}

