package com.example.company.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String position;

    @PositiveOrZero
    @Column(nullable = false)
    private Double salary;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}