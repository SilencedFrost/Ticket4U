package com.ticket4u.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "order", schema = "public")
@NoArgsConstructor
public class Order {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(updatable = false, nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String currency;

    private String discountCode;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal fees;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String status;

    private String paymentMethod;

    @Column(nullable = false, length = 32)
    private String paymentStatus;

    private String transactionId;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime purchasedAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    private OffsetDateTime cancelledAt;

    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(columnDefinition = "text")
    private String notes;

    @OneToMany(mappedBy = "order")
    private Set<Ticket> tickets = new HashSet<>();
}