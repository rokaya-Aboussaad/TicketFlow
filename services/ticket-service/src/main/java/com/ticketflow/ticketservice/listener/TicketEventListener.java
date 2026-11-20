package com.ticketflow.ticketservice.listener;

import com.ticketflow.ticketservice.domain.Ticket;
import com.ticketflow.ticketservice.repo.TicketRepository;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TicketEventListener {

    private final TicketRepository repo;

    public TicketEventListener(TicketRepository r) {
        repo = r;
    }

    @KafkaListener(topics = "booking-confirmed", groupId = "ticket-service")
    @Transactional
    public void onConfirmed(Map<String, Object> e) {
        Long booking = Long.valueOf(e.get("bookingId").toString());
        if (!repo.findByBookingId(booking).isEmpty()) return;
        Long customer = Long.valueOf(e.get("customerId").toString());
        Long event = Long.valueOf(e.get("eventId").toString());
        int quantity = Integer.parseInt(e.get("quantity").toString());
        for (int i = 0; i < quantity; i++) repo.save(new Ticket(booking, customer, event));
    }
}
