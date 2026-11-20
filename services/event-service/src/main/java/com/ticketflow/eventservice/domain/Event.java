package com.ticketflow.eventservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String venue;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String status = "PUBLISHED";

    protected Event() {}

    public Event(
        String title,
        String description,
        String venue,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer capacity,
        BigDecimal price
    ) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.capacity = capacity;
        this.availableSeats = capacity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public void update(
        String title,
        String description,
        String venue,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer capacity,
        BigDecimal price
    ) {
        if (capacity < this.capacity - this.availableSeats) throw new IllegalArgumentException(
            "La capacité ne peut être inférieure aux places déjà réservées"
        );
        int sold = this.capacity - this.availableSeats;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.capacity = capacity;
        this.availableSeats = capacity - sold;
        this.price = price;
    }

    public boolean reserve(int quantity) {
        if (!"PUBLISHED".equals(status) || quantity < 1 || availableSeats < quantity) return false;
        availableSeats -= quantity;
        return true;
    }

    public void release(int quantity) {
        availableSeats = Math.min(capacity, availableSeats + quantity);
    }
}
