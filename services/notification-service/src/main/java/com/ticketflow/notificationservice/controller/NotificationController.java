package com.ticketflow.notificationservice.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "mode", "console-adapter");
    }
}
