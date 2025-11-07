package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    boolean existsById(Long id);
    Book save(Book book);
    Book update(Long id, Book book);
    void deleteById(Long id);
}
