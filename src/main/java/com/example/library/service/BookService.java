package com.example.library.service;

import com.example.library.dto.AuthorResponse;
import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.util.error.NotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<BookResponse> list(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toResponse);
    }

    public Page<BookResponse> listByAuthor(Long authorId, Pageable pageable) {
        if (!authorRepository.existsById(authorId)) {
            throw new NotFoundException("Author %d not found".formatted(authorId));
        }
        return bookRepository.findByAuthor_Id(authorId, pageable)
                .map(this::toResponse);
    }

    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));
        return toResponse(book);
    }

    public BookResponse create(BookRequest req) {
        Author author = authorRepository.findById(req.authorId())
                .orElseThrow(() -> new NotFoundException("Author %d not found".formatted(req.authorId())));

        Book book = Book.builder()
                .title(req.title())
                .yearPublished(req.yearPublished())
                .author(author)
                .build();

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public BookResponse update(Long id, BookRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book %d not found".formatted(id)));

        Author author = authorRepository.findById(req.authorId())
                .orElseThrow(() -> new NotFoundException("Author %d not found".formatted(req.authorId())));

        book.setTitle(req.title());
        book.setYearPublished(req.yearPublished());
        book.setAuthor(author);

        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book %d not found".formatted(id));
        }
        bookRepository.deleteById(id);
    }

    private BookResponse toResponse(Book b) {
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getYearPublished(),
                new AuthorResponse(
                        b.getAuthor().getId(),
                        b.getAuthor().getName()
                )
        );
    }
}