package com.example.onlinestore;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository repo;

    @InjectMocks
    ProductService service;

    @Test
    void save_thenGetById() {
        Product p = new Product(null, "Phone", "desc", 1000.0, 3);
        Product saved = new Product(1L, "Phone", "desc", 1000.0, 3);

        when(repo.save(p)).thenReturn(saved);
        when(repo.findById(1L)).thenReturn(Optional.of(saved));

        Product resSaved = service.create(p);
        Product resFetched = service.getById(1L);

        assertThat(resSaved.getProductId()).isEqualTo(1L);
        assertThat(resFetched.getName()).isEqualTo("Phone");

        verify(repo).save(p);
        verify(repo).findById(1L);
    }
}