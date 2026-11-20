package com.ticketflow.bookingservice.repo;

import com.ticketflow.bookingservice.domain.Booking;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdempotencyKey(String key);
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByStatusAndExpiresAtBefore(String status, Instant now);
}
