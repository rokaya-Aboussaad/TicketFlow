package com.ticketflow.paymentservice.exception;

import feign.FeignException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", String.valueOf(ex.getMessage())));
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, String>> bookingNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Réservation introuvable"));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> bookingServiceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of("error", "Booking service indisponible")
        );
    }
}
