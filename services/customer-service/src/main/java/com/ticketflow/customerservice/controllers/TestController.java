package com.ticketflow.customerservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${custom.greeting}")
    private String greeting;

    @GetMapping("/test")
    public String test() {
        return greeting;
    }
}