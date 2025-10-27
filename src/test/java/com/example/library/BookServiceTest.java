package com.example.library;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookServiceTest {

    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;

    BookService service;

    @BeforeEach
    void setUp() {
        service = new BookService(bookRepository, authorRepository);
    }

    @Test
    void list_returnsPagedData() {
        Page<BookResponse> page = service.list(PageRequest.of(0, 2));
        assertEquals(2, page.getSize());
        assertTrue(page.getTotalElements() > 0);
    }

    @Test
    void create_persistsAndReturnsDto() {
        BookRequest req = new BookRequest("Minimal Book", 2024, 1L);
        BookResponse created = service.create(req);
        assertNotNull(created.id());
        assertEquals("Minimal Book", created.title());
    }
}