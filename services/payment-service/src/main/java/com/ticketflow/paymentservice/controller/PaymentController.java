package com.ticketflow.paymentservice.controller;

import com.ticketflow.paymentservice.domain.Payment;
import com.ticketflow.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService s) {
        service = s;
    }

    public record PayRequest(@NotNull @Positive Long bookingId, Boolean simulateSuccess) {}

    @PostMapping
    public Payment pay(@Valid @RequestBody PayRequest r) {
        return service.pay(r.bookingId(), !Boolean.FALSE.equals(r.simulateSuccess()));
    }
}
