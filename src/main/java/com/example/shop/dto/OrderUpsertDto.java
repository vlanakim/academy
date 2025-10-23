package com.example.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
public record OrderUpsertDto(
        @NotBlank String items,
        @DecimalMin("0.0") BigDecimal total,
        @NotBlank String status
) {}
