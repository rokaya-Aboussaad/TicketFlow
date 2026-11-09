package com.ticketflow.bookingservice.controller;

import com.ticketflow.bookingservice.client.EventClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final EventClient eventClient;

    // Injection automatique du client Feign
    public BookingController(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    @GetMapping("/check/{eventId}")
    public Map<String, Object> checkEvent(@PathVariable Long eventId) {
        // Appel transparent à event-service via OpenFeign et Eureka
        Map<String, Object> event = eventClient.getEventById(eventId);

        return Map.of(
                "bookingStatus", "CONFIRMED",
                "eventDetails", event
        );
    }
}