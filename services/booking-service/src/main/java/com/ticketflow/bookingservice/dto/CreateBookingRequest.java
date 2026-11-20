package com.ticketflow.bookingservice.dto;

import jakarta.validation.constraints.*;

public record CreateBookingRequest(
    @NotNull @Positive Long customerId,
    @NotNull @Positive Long eventId,
    @NotNull @Min(1) @Max(10) Integer quantity
) {}
