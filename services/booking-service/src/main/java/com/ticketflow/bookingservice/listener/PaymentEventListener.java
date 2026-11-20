package com.ticketflow.bookingservice.listener;

import com.ticketflow.bookingservice.domain.Booking;
import com.ticketflow.bookingservice.service.BookingService;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private final BookingService bookings;
    private final KafkaTemplate<String, Object> kafka;

    public PaymentEventListener(BookingService b, KafkaTemplate<String, Object> k) {
        bookings = b;
        kafka = k;
    }

    @KafkaListener(topics = "payment-events", groupId = "booking-service")
    public void onPayment(Map<String, Object> e) {
        Long id = Long.valueOf(e.get("bookingId").toString());
        if ("PAYMENT_FAILED".equals(e.get("eventType"))) {
            bookings.paymentFailed(id);
            return;
        }
        if (!"PAYMENT_SUCCEEDED".equals(e.get("eventType"))) return;
        Booking b = bookings.confirm(id);
        kafka.send(
            "booking-confirmed",
            id.toString(),
            Map.of(
                "bookingId",
                id,
                "customerId",
                b.getCustomerId(),
                "eventId",
                b.getEventId(),
                "quantity",
                b.getQuantity(),
                "totalPrice",
                b.getTotalPrice()
            )
        );
    }
}
