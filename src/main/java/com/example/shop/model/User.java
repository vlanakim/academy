package com.example.shop.model;

import com.example.shop.json.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    private Long id;

    @NotBlank
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    private String name;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders = new ArrayList<>();

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Order> getOrders() { return orders; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}