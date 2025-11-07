package com.example.libraryjdbc.service;

import com.example.libraryjdbc.model.Book;
import com.example.libraryjdbc.repository.BookRepository;
import com.example.libraryjdbc.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repo;

    public Book create(Book book) {
        return repo.save(book);
    }

    public Book getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));
    }

    public List<Book> getAll() {
        return repo.findAll();
    }

    public Book update(Long id, Book updated) {
        Book existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));

        if (updated.getTitle() != null) {
            if (!StringUtils.hasText(updated.getTitle())) {
                throw new IllegalArgumentException("Title cannot be blank.");
            }
            existing.setTitle(updated.getTitle());
        }

        if (updated.getAuthor() != null) {
            if (!StringUtils.hasText(updated.getAuthor())) {
                throw new IllegalArgumentException("Author cannot be blank.");
            }
            existing.setAuthor(updated.getAuthor());
        }

        if (updated.getPublicationYear() != null) {
            int y = updated.getPublicationYear();
            int current = Year.now().getValue();
            if (y < 1400 || y > current) {
                throw new IllegalArgumentException("Publication year must be between 1400 and " + current + ".");
            }
            existing.setPublicationYear(y);
        }

        return repo.update(id, existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Book %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}