package com.ticket4u.core;

import com.ticket4u.core.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zones", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Zone {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(length = 255)
    private String name;

    @Column(name = "is_standing")
    private Boolean isStanding = false;

    @Column
    private Integer capacity = 0;

    @Column(name = "quantity_sold")
    private Integer quantitySold = 0;

    @Column(name = "purchase_limit")
    private Integer purchaseLimit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Zone content fields (gộp từ ZoneContent)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "gift_image_url", length = 512)
    private String giftImageUrl;

    @Column(columnDefinition = "JSONB")
    @Convert(converter = StringListConverter.class)
    private List<String> perks;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    private List<Seat> seats;
}
