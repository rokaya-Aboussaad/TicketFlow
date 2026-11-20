package com.ticketflow.eventservice.controller;

import com.ticketflow.eventservice.domain.Event;
import com.ticketflow.eventservice.dto.EventRequest;
import com.ticketflow.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    // Injection de dépendance propre via le constructeur
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public Event get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody EventRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody EventRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/reservations")
    public Map<String, Object> reserve(@PathVariable Long id, @RequestParam int quantity) {
        service.reserve(id, quantity);
        return Map.of("eventId", id, "reserved", quantity);
    }

    @PostMapping("/{id}/releases")
    public Map<String, Object> release(@PathVariable Long id, @RequestParam int quantity) {
        service.release(id, quantity);
        return Map.of("eventId", id, "released", quantity);
    }
}