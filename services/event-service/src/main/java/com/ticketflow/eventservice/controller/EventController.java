package com.ticketflow.eventservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @GetMapping("/{id}")
    public Map<String, Object> getEvent(@PathVariable Long id) {
        return Map.of(
                "id", id,
                "name", "Concert de Rock",
                "available", true
        );
    }
}