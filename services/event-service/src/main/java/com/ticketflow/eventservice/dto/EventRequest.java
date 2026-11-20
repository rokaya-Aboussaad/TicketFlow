package com.ticketflow.eventservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequest(
    @NotBlank String title,
    String description,
    @NotBlank String venue,
    @NotNull LocalDateTime startsAt,
    @NotNull LocalDateTime endsAt,
    @NotNull @Min(1) Integer capacity,
    @NotNull @DecimalMin("0.0") BigDecimal price
) {}
