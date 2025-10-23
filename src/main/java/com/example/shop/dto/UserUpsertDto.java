package com.example.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record UserUpsertDto(
        @NotBlank String name,
        @NotBlank @Email String email
) {}