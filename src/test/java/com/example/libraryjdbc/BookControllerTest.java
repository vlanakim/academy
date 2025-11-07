package com.example.libraryjdbc;

import com.example.libraryjdbc.controller.BookController;
import com.example.libraryjdbc.model.Book;
import com.example.libraryjdbc.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private BookService service;

    @Test
    void create_valid_returns201_and_body() throws Exception {
        Book input = new Book(null, "Clean Code", "Robert C. Martin", 2008);
        Book saved = new Book(1L, input.getTitle(), input.getAuthor(), input.getPublicationYear());
        when(service.create(any(Book.class))).thenReturn(saved);

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void create_invalid_returns400_from_bean_validation() throws Exception {
        String badJson = """
      { "title": "   ", "author": "A", "publicationYear": 2008 }
      """;
        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
}