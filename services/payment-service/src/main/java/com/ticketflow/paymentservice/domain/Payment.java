package com.ticketflow.paymentservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bookingId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(name = "payment_reference", nullable = false, unique = true)
    private String reference = UUID.randomUUID().toString();

    private Instant createdAt = Instant.now();

    protected Payment() {}

    public Payment(Long b, Long c, BigDecimal a, String s) {
        bookingId = b;
        customerId = c;
        amount = a;
        status = s;
    }

    public Long getId() {
        return id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
