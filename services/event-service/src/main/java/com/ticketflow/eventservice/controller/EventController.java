package com.ticketflow.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @GetMapping("/{id}")
    public Map<String, Object> getEvent(@PathVariable Long id) {
        if (id == 999L) {
            // On lance une exception spécifique que Feign interprétera comme 404
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Event not found"
            );
        }
        return Map.of("id", id, "name", "Concert de Rock", "available", true);
    }
}