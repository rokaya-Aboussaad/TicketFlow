package com.ticketflow.ticketservice.controller;

import com.ticketflow.ticketservice.domain.Ticket;
import com.ticketflow.ticketservice.repo.TicketRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository r;

    public TicketController(TicketRepository r) {
        this.r = r;
    }

    @GetMapping("/booking/{id}")
    public List<Ticket> byBooking(@PathVariable Long id) {
        return r.findByBookingId(id);
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return r
            .findById(id)
            .orElseThrow(() ->
                new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND
                )
            );
    }
}
