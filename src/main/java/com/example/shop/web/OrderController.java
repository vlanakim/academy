package com.example.shop.web;

import com.example.shop.dto.OrderUpsertDto;
import com.example.shop.json.Views;
import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/orders")
public class OrderController {
    private final OrderRepository orders;
    private final UserRepository users;

    public OrderController(OrderRepository orders, UserRepository users) {
        this.orders = orders;
        this.users = users;
    }

    private User userOr404(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User %d not found".formatted(userId)));
    }

    @GetMapping
    @JsonView(Views.OrderSummary.class)
    public List<Order> list(@PathVariable Long userId) {
        return orders.findByUser(userOr404(userId));
    }

    @GetMapping("/{orderId}")
    @JsonView(Views.OrderDetails.class)
    public Order get(@PathVariable Long userId, @PathVariable Long orderId) {
        userOr404(userId);
        return orders.findById(orderId)
                .filter(ord -> ord.getUser() != null && ord.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Order %d not found for user %d".formatted(orderId, userId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Views.OrderDetails.class)
    public Order create(@PathVariable Long userId, @Valid @RequestBody OrderUpsertDto dto) {
        User user = userOr404(userId);
        Order order = new Order();
        order.setItems(dto.items());
        order.setTotal(dto.total());
        order.setStatus(dto.status());
        order.setUser(user);
        return orders.save(order);
    }

    @PutMapping("/{orderId}")
    @JsonView(Views.OrderDetails.class)
    public Order update(@PathVariable Long userId, @PathVariable Long orderId, @Valid @RequestBody OrderUpsertDto dto) {
        userOr404(userId);
        Order order = orders.findById(orderId)
                .filter(ord -> ord.getUser() != null && ord.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Order %d not found for user %d".formatted(orderId, userId)));
        order.setItems(dto.items());
        order.setTotal(dto.total());
        order.setStatus(dto.status());
        return orders.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long orderId) {
        userOr404(userId);
        Order order = orders.findById(orderId)
                .filter(ord -> ord.getUser() != null && ord.getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Order %d not found for user %d".formatted(orderId, userId)));
        orders.delete(order);
    }
}