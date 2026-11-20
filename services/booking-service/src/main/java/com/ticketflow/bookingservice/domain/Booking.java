package com.ticketflow.bookingservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String status = "PENDING_PAYMENT";

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private Instant expiresAt;

    private Instant createdAt = Instant.now();

    protected Booking() {}

    public Booking(Long c, Long e, int q, BigDecimal price, String key, Instant expiry) {
        customerId = c;
        eventId = e;
        quantity = q;
        unitPrice = price;
        totalPrice = price.multiply(BigDecimal.valueOf(q));
        idempotencyKey = key;
        expiresAt = expiry;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void confirm() {
        if ("CONFIRMED".equals(status)) return;
        if (!"PENDING_PAYMENT".equals(status) || expiresAt.isBefore(Instant.now())) throw new IllegalStateException(
            "Réservation expirée ou non payable"
        );
        status = "CONFIRMED";
    }

    public void cancel() {
        if ("PENDING_PAYMENT".equals(status)) status = "CANCELLED";
    }

    public void expire() {
        if ("PENDING_PAYMENT".equals(status)) status = "EXPIRED";
    }
}
