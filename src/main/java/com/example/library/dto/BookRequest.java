package com.example.library.dto;

import jakarta.validation.constraints.*;
public record BookRequest(
        @NotBlank(message = "Title is required") String title,
        @Min(0) @Max(2100) Integer yearPublished,
        @NotNull(message = "authorId is required") Long authorId
) {}