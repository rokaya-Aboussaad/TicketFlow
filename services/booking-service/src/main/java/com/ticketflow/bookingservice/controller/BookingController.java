package com.ticketflow.bookingservice.controller;

import com.ticketflow.bookingservice.domain.Booking;
import com.ticketflow.bookingservice.dto.CreateBookingRequest;
import com.ticketflow.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService s;

    public BookingController(BookingService s) {
        this.s = s;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@Valid @RequestBody CreateBookingRequest r, @RequestHeader("Idempotency-Key") String key) {
        return s.create(r, key);
    }

    @GetMapping("/{id}")
    public Booking get(@PathVariable Long id) {
        return s.get(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Booking> customer(@PathVariable Long customerId) {
        return s.byCustomer(customerId);
    }
}
