package com.example.libraryjdbc;

import com.example.libraryjdbc.model.Book;
import com.example.libraryjdbc.repository.BookRepository;
import com.example.libraryjdbc.service.BookService;
import com.example.libraryjdbc.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock BookRepository repo;
    @InjectMocks BookService service;

    @Test
    void update_partial_ok() {
        when(repo.findById(5L)).thenReturn(Optional.of(new Book(5L, "Old", "Same", 2000)));
        when(repo.update(eq(5L), any(Book.class))).thenAnswer(inv -> inv.getArgument(1, Book.class));

        Book patch = new Book(null, "New", null, null);
        Book result = service.update(5L, patch);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitle()).isEqualTo("New");
        assertThat(result.getAuthor()).isEqualTo("Same");
        assertThat(result.getPublicationYear()).isEqualTo(2000);
    }

    @Test
    void getById_notFound_throws404() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book 99 not found");
    }
}