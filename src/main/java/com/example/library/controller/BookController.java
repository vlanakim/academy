package com.example.library.controller;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public Page<BookResponse> list(
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({ @SortDefault(sort = "title") })
            Pageable pageable
    ) {
        return bookService.list(pageable);
    }

    @GetMapping("/author/{authorId}")
    public Page<BookResponse> listByAuthor(
            @PathVariable Long authorId,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({ @SortDefault(sort = "title") })
            Pageable pageable
    ) {
        return bookService.listByAuthor(authorId, pageable);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest req) {
        return bookService.create(req);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest req) {
        return bookService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}