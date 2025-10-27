package com.example.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books", indexes = { @Index(name = "idx_books_title", columnList = "title") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 250)
    private String title;

    private Integer yearPublished;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}