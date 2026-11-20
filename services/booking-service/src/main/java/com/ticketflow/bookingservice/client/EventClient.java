package com.ticketflow.bookingservice.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "event-service")
public interface EventClient {
    @GetMapping("/api/events/{id}")
    EventView getEvent(@PathVariable("id") Long id);

    @PostMapping("/api/events/{id}/reservations")
    void reserve(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);

    @PostMapping("/api/events/{id}/releases")
    void release(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);

    record EventView(
        Long id,
        String title,
        String description,
        String venue,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer capacity,
        Integer availableSeats,
        BigDecimal price,
        String status
    ) {}
}
