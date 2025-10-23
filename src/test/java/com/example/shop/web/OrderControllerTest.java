package com.example.shop.web;

import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired MockMvc mvc;

    @MockitoBean OrderRepository orders;
    @MockitoBean UserRepository users;

    private User u(long id) {
        var x = new User();
        x.setId(id);
        return x;
    }

    private Order order(User u, long id) {
        var o = new Order();
        o.setId(id);
        o.setItems("SKU-1,SKU-2");
        o.setTotal(new BigDecimal("149.90"));
        o.setStatus("PAID");
        o.setUser(u);
        return o;
    }

    @Test
    void listOrders_usesOrderSummary_withoutUserField() throws Exception {
        var user = u(1L);
        Mockito.when(users.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(orders.findByUser(user)).thenReturn(List.of(order(user, 10L)));

        mvc.perform(get("/api/users/1/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].items").value("SKU-1,SKU-2"))
                .andExpect(jsonPath("$[0].total").value(149.90))
                .andExpect(jsonPath("$[0].status").value("PAID"))
                .andExpect(jsonPath("$[0].user").doesNotExist());
    }

    @Test
    void getOrder_usesOrderDetails_stillWithoutUserField() throws Exception {
        var user = u(1L);
        Mockito.when(users.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(orders.findById(11L)).thenReturn(Optional.of(order(user, 11L)));

        mvc.perform(get("/api/users/1/orders/11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.items").value("SKU-1,SKU-2"))
                .andExpect(jsonPath("$.total").value(149.90))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.user").doesNotExist());
    }
}