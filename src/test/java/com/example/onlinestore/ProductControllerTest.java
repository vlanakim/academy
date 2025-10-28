package com.example.onlinestore;

import com.example.onlinestore.controller.ProductController;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductService service;

    @Test
    void createProduct_returnsJsonProduct() throws Exception {
        Product incoming = new Product(null, "Kbd", "Mech", 150.0, 10);
        Product created = new Product(5L, "Kbd", "Mech", 150.0, 10);

        Mockito.when(service.create(any(Product.class))).thenReturn(created);

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is(5)))
                .andExpect(jsonPath("$.name", is("Kbd")));
    }

    @Test
    void getAll_returnsArray() throws Exception {
        Mockito.when(service.getAll()).thenReturn(
                List.of(new Product(1L,"Phone","",1000.0,3))
        );

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Phone")));
    }
}