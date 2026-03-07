package com.ticket4u.core.entity;

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
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private EventSession session;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "is_standing", nullable = false)
    private Boolean isStanding;

    @Column(nullable = true)
    private Integer capacity;

    @Column(name = "quantity_sold", nullable = true)
    private Integer quantitySold;

    @Column(name = "purchase_limit", nullable = true)
    private Integer purchaseLimit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description_vi", nullable = true, columnDefinition = "TEXT")
    private String descriptionVi;

    @Column(name = "description_en", nullable = true, columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "gift_image_url", nullable = true, length = 512)
    private String giftImageUrl;

    @Column(columnDefinition = "JSONB")
    @Convert(converter = StringListConverter.class)
    private List<String> perks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    private List<Seat> seats;
}
