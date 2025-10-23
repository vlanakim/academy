package com.example.shop.model;

import com.example.shop.json.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.OrderSummary.class, Views.OrderDetails.class, Views.UserDetails.class})
    private Long id;

    @NotBlank
    @JsonView({Views.OrderSummary.class, Views.OrderDetails.class, Views.UserDetails.class})
    private String items;

    @DecimalMin("0.0")
    @JsonView({Views.OrderSummary.class, Views.OrderDetails.class, Views.UserDetails.class})
    private BigDecimal total;

    @NotBlank
    @JsonView({Views.OrderSummary.class, Views.OrderDetails.class, Views.UserDetails.class})
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Long getId() { return id; }
    public String getItems() { return items; }
    public BigDecimal getTotal() { return total; }
    public String getStatus() { return status; }
    public User getUser() { return user; }
    public void setId(Long id) { this.id = id; }
    public void setItems(String items) { this.items = items; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setStatus(String status) { this.status = status; }
    public void setUser(User user) { this.user = user; }
}