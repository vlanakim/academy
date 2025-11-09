package com.example.company;

import com.example.company.model.Department;
import com.example.company.repository.DepartmentRepository;
import com.example.company.service.DepartmentService;
import com.example.company.util.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    DepartmentRepository repo;
    @InjectMocks
    DepartmentService service;

    @Test
    void update_partial_ok() {
        when(repo.findById(5L)).thenReturn(Optional.of(Department.builder().id(5L).name("Old").build()));
        when(repo.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        Department patch = Department.builder().name("New").build();
        Department result = service.update(5L, patch);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getName()).isEqualTo("New");
    }

    @Test
    void findById_notFound_throws404() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department 99 not found");
    }
}