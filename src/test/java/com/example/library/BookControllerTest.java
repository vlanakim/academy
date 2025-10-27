package com.example.library;

import com.example.library.controller.BookController;
import com.example.library.dto.AuthorResponse;
import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.util.error.GlobalExceptionHandler;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
public class BookControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    BookService bookService;

    @Test
    void list_basicOk() throws Exception {
        var content = List.of(
                new BookResponse(1L, "A", 2000, new AuthorResponse(10L, "Author")),
                new BookResponse(2L, "B", 2001, new AuthorResponse(10L, "Author"))
        );
        when(bookService.list(any())).thenAnswer(inv ->
                new PageImpl<>(content, (PageRequest) inv.getArgument(0), 2)
        );

        mvc.perform(get("/api/books?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is("A")));
    }

    @Test
    void create_validationError_returns400() throws Exception {
        var invalid = new BookRequest("  ", 2024, 1L);

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", not(emptyOrNullString())));
    }
}
