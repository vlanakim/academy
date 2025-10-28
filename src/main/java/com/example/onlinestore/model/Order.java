package com.example.onlinestore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    private List<Product> products;

    private LocalDateTime orderDate;
    private String shippingAddress;
    private double totalPrice;
    private String orderStatus;
}