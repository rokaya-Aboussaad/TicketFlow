package com.ticketflow.bookingservice.service;

import com.ticketflow.bookingservice.client.EventClient;
import com.ticketflow.bookingservice.domain.Booking;
import com.ticketflow.bookingservice.dto.CreateBookingRequest;
import com.ticketflow.bookingservice.repo.BookingRepository;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository repo;
    private final EventClient events;
    private final com.ticketflow.bookingservice.client.CustomerClient customers;

    public BookingService(BookingRepository r, EventClient e, com.ticketflow.bookingservice.client.CustomerClient c) {
        repo = r;
        events = e;
        customers = c;
    }

    @Transactional
    public Booking create(CreateBookingRequest r, String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Header Idempotency-Key obligatoire");
        var previous = repo.findByIdempotencyKey(key);
        if (previous.isPresent()) return previous.get();
        customers.getCustomer(r.customerId());
        var event = events.getEvent(r.eventId());
        if (!"PUBLISHED".equals(event.status())) throw new IllegalStateException(
            "Événement non disponible à la réservation"
        );
        events.reserve(r.eventId(), r.quantity());
        try {
            return repo.save(
                new Booking(
                    r.customerId(),
                    r.eventId(),
                    r.quantity(),
                    event.price(),
                    key,
                    Instant.now().plus(Duration.ofMinutes(15))
                )
            );
        } catch (RuntimeException ex) {
            events.release(r.eventId(), r.quantity());
            throw ex;
        }
    }

    public Booking get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Réservation introuvable"));
    }

    public List<Booking> byCustomer(Long id) {
        return repo.findByCustomerId(id);
    }

    @Transactional
    public void expirePending() {
        for (Booking b : repo.findByStatusAndExpiresAtBefore("PENDING_PAYMENT", Instant.now())) {
            b.expire();
            events.release(b.getEventId(), b.getQuantity());
        }
    }

    @Transactional
    public Booking confirm(Long id) {
        Booking b = get(id);
        b.confirm();
        return b;
    }

    @Transactional
    public void paymentFailed(Long id) {
        Booking b = get(id);
        if ("PENDING_PAYMENT".equals(b.getStatus())) {
            b.cancel();
            events.release(b.getEventId(), b.getQuantity());
        }
    }
}
