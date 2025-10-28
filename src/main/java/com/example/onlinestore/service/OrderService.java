package com.example.onlinestore.service;

import com.example.onlinestore.model.Order;
import com.example.onlinestore.repository.OrderRepository;
import com.example.onlinestore.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order create(Order order) {
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty.");
        }
        if (order.getShippingAddress() == null || order.getShippingAddress().isBlank()) {
            throw new IllegalArgumentException("Shipping address is required.");
        }

        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("NEW");
        order.setTotalPrice(order.getProducts().stream()
                .mapToDouble(p -> p.getPrice())
                .sum());

        return repo.save(order);
    }

    public Order getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(id)));
    }

    public List<Order> getAll() {
        return repo.findAll();
    }

    public Order update(Long id, Order updated) {
        return repo.findById(id)
                .map(existing -> {
                    if (updated.getShippingAddress() != null && !updated.getShippingAddress().isBlank()) {
                        existing.setShippingAddress(updated.getShippingAddress());
                    }
                    if (updated.getOrderStatus() != null && !updated.getOrderStatus().isBlank()) {
                        existing.setOrderStatus(updated.getOrderStatus());
                    }
                    return repo.save(existing);
                })
                .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(id)));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Order %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}
