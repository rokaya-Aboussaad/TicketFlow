package com.ticketflow.paymentservice.client;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service")
public interface BookingClient {
    @GetMapping("/api/bookings/{id}")
    BookingView get(@PathVariable("id") Long id);

    record BookingView(
        Long id,
        Long customerId,
        Long eventId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String status
    ) {}
}
