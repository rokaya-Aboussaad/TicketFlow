package com.ticketflow.bookingservice.exception;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Si c'est un 404 (événement introuvable), on garde le 404.
    // Resilience4j ignore cette exception, elle n'ouvre PAS le circuit.
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 2. TOUTES les autres erreurs Feign (Connection refused, Timeout,
    // ou Circuit OPEN) doivent devenir un 503 Service Unavailable.
    @ExceptionHandler({FeignException.class, CallNotPermittedException.class})
    public ResponseEntity<Void> handleServiceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}