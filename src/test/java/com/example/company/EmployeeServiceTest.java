package com.example.company;

import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.repository.DepartmentRepository;
import com.example.company.repository.EmployeeRepository;
import com.example.company.service.EmployeeService;
import com.example.company.util.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    EmployeeRepository repo;
    @Mock
    DepartmentRepository deptRepo;

    @InjectMocks
    EmployeeService service;

    @Test
    void update_partial_ok() {

        Department oldDept = Department.builder().id(1L).name("HR").build();
        Employee existing = Employee.builder()
                .id(5L).firstName("Bob").lastName("Smith")
                .position("HR Manager").salary(2000.0)
                .department(oldDept)
                .build();

        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee patch = Employee.builder()
                .position("Lead")
                .salary(2500.0)
                .build();

        Employee result = service.update(5L, patch, null);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getPosition()).isEqualTo("Lead");
        assertThat(result.getSalary()).isEqualTo(2500.0);
        assertThat(result.getFirstName()).isEqualTo("Bob");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getDepartment().getId()).isEqualTo(1L);
    }

    @Test
    void findById_notFound_throws404() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Employee 99 not found");
    }
}