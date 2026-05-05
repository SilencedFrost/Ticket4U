package com.ticket4u.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment_webhook_event", schema = "public")
@NoArgsConstructor
public class PaymentWebhookEvent {

    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false, length = 16)
    private String transferType;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal transferAmount;

    @Column(length = 64)
    private String gateway;

    @Column(length = 255)
    private String referenceCode;

    @Column(length = 1024)
    private String content;

    @Column(nullable = false)
    private OffsetDateTime receivedAt;
}
