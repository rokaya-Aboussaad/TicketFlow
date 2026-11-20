package com.ticketflow.notificationservice.listener;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(topics = "booking-confirmed", groupId = "notification-service")
    public void bookingConfirmed(Map<String, Object> event) {
        log.info(
            "[NOTIFICATION DEMO] Réservation confirmée: bookingId={}, customerId={}, eventId={}",
            event.get("bookingId"),
            event.get("customerId"),
            event.get("eventId")
        );
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-service")
    public void paymentEvent(Map<String, Object> event) {
        log.info("[NOTIFICATION DEMO] Événement paiement reçu: {}", event);
    }
}
