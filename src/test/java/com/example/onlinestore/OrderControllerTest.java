package com.example.onlinestore;

import com.example.onlinestore.controller.OrderController;
import com.example.onlinestore.model.Order;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrderService service;

    @Test
    void createOrder_returnsJsonOrder() throws Exception {
        Order incoming = new Order();
        incoming.setProducts(List.of(new Product(1L, "Phone", "desc", 1000.0, 3)));
        incoming.setShippingAddress("Test Address");

        Order created = new Order();
        created.setOrderId(10L);
        created.setProducts(incoming.getProducts());
        created.setShippingAddress("Test Address");
        created.setOrderStatus("NEW");
        created.setTotalPrice(1000.0);

        Mockito.when(service.create(any(Order.class))).thenReturn(created);

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(10)))
                .andExpect(jsonPath("$.orderStatus", is("NEW")))
                .andExpect(jsonPath("$.shippingAddress", is("Test Address")))
                .andExpect(jsonPath("$.totalPrice", is(1000.0)));
    }

    @Test
    void getOrderById_returnsJsonOrder() throws Exception {
        Order order = new Order();
        order.setOrderId(7L);
        order.setShippingAddress("City Street");
        order.setOrderStatus("NEW");
        order.setTotalPrice(500.0);

        Mockito.when(service.getById(7L)).thenReturn(order);

        mvc.perform(get("/api/orders/7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(7)))
                .andExpect(jsonPath("$.orderStatus", is("NEW")))
                .andExpect(jsonPath("$.totalPrice", is(500.0)));
    }
}