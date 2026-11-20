package com.ticketflow.ticketservice.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false, unique = true)
    private String code = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String status = "VALID";

    private Instant issuedAt = Instant.now();

    protected Ticket() {}

    public Ticket(Long b, Long c, Long e) {
        bookingId = b;
        customerId = c;
        eventId = e;
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

    public Long getEventId() {
        return eventId;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }
}
