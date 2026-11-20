package com.ticketflow.ticketservice.repo;

import com.ticketflow.ticketservice.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByBookingId(Long bookingId);
}
