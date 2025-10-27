package com.example.library.dto;

public record BookResponse(
        Long id,
        String title,
        Integer yearPublished,
        AuthorResponse author
) {}