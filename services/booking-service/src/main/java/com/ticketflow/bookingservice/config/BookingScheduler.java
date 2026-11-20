package com.ticketflow.bookingservice.config;

import com.ticketflow.bookingservice.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingScheduler {

    private final BookingService service;

    public BookingScheduler(BookingService s) {
        service = s;
    }

    @Scheduled(fixedDelayString = "${ticketflow.booking.expiration-scan-ms:30000}")
    public void expire() {
        service.expirePending();
    }
}
