package com.ticketflow.eventservice.controller;

import com.ticketflow.eventservice.domain.Event;
import com.ticketflow.eventservice.dto.EventRequest;
import com.ticketflow.eventservice.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService s) {
        service = s;
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
    public Event create(@Valid @RequestBody EventRequest r) {
        return service.create(r);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody EventRequest r) {
        return service.update(id, r);
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
