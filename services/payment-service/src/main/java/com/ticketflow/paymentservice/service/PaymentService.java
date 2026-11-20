package com.ticketflow.paymentservice.service;

import com.ticketflow.paymentservice.client.BookingClient;
import com.ticketflow.paymentservice.domain.Payment;
import com.ticketflow.paymentservice.repo.PaymentRepository;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final KafkaTemplate<String, Object> kafka;
    private final BookingClient bookings;

    public PaymentService(PaymentRepository r, KafkaTemplate<String, Object> k, BookingClient b) {
        repo = r;
        kafka = k;
        bookings = b;
    }

    @Transactional
    public Payment pay(Long bookingId, boolean success) {
        var old = repo.findByBookingId(bookingId);
        if (old.isPresent()) return old.get();
        var booking = bookings.get(bookingId);
        if (!"PENDING_PAYMENT".equals(booking.status())) throw new IllegalStateException(
            "Cette réservation n'attend plus de paiement"
        );
        Payment p = repo.save(
            new Payment(bookingId, booking.customerId(), booking.totalPrice(), success ? "SUCCEEDED" : "FAILED")
        );
        kafka.send(
            "payment-events",
            bookingId.toString(),
            Map.of(
                "eventType",
                success ? "PAYMENT_SUCCEEDED" : "PAYMENT_FAILED",
                "bookingId",
                bookingId,
                "customerId",
                booking.customerId(),
                "amount",
                booking.totalPrice(),
                "paymentReference",
                p.getReference()
            )
        );
        return p;
    }
}
