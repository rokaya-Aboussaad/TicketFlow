package com.ticketflow.bookingservice.exception;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erreur 4xx renvoyée par un autre service (événement/client introuvable, places indisponibles...) :
    // on garde le même code. Le circuit breaker ne s'ouvre pas pour ces erreurs métier.
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> downstream(FeignException ex) {
        int status = ex.status();
        if (status >= 400 && status < 500) {
            return ResponseEntity.status(status).body(
                Map.of("error", "Requête refusée par un service distant (" + status + ")")
            );
        }
        // Connexion refusée, timeout, erreur 5xx : service indisponible
        return unavailable();
    }

    // Circuit ouvert
    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Map<String, String>> circuitOpen() {
        return unavailable();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", String.valueOf(ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", String.valueOf(ex.getMessage())));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", String.valueOf(ex.getMessage())));
    }

    private ResponseEntity<Map<String, String>> unavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of("error", "Service distant indisponible")
        );
    }
}
