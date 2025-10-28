package com.example.onlinestore;

import com.example.onlinestore.model.Order;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.OrderRepository;
import com.example.onlinestore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository repo;

    @InjectMocks
    OrderService service;

    @Test
    void createOrder_setsDateStatusAndTotal() {
        Product p1 = new Product(1L, "A", "desc", 10.0, 5);
        Product p2 = new Product(2L, "B", "desc", 20.0, 7);

        Order incoming = new Order();
        incoming.setProducts(List.of(p1, p2));
        incoming.setShippingAddress("Addr");

        Order persisted = new Order();
        persisted.setOrderId(100L);
        persisted.setProducts(incoming.getProducts());
        persisted.setShippingAddress("Addr");
        persisted.setOrderStatus("NEW");
        persisted.setTotalPrice(30.0);
        persisted.setOrderDate(LocalDateTime.now());

        when(repo.save(any(Order.class))).thenReturn(persisted);

        Order result = service.create(incoming);

        assertThat(result.getOrderId()).isEqualTo(100L);
        assertThat(result.getOrderStatus()).isEqualTo("NEW");
        assertThat(result.getTotalPrice()).isEqualTo(30.0);
        assertThat(result.getOrderDate()).isNotNull();

        verify(repo).save(any(Order.class));
    }
}