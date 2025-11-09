package com.example.company.controller;

import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import com.example.company.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public List<Employee> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody @Valid Employee employee) {

        if (employee.getDepartment() == null || employee.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Department ID is required.");
        }
        return service.create(employee, employee.getDepartment().getId());
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        if (employee.getDepartment() == null || employee.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Department ID is required.");
        }
        return service.update(id, employee, employee.getDepartment().getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/projection")
    public List<EmployeeProjection> projectionAll() {
        return service.findAllProjected();
    }

    @GetMapping("/projection/by-department")
    public List<EmployeeProjection> projectionByDepartment(@RequestParam String name) {
        return service.findByDepartmentName(name);
    }

    @GetMapping("/projection/by-position")
    public List<EmployeeProjection> projectionByPosition(@RequestParam String position) {
        return service.findByPosition(position);
    }
}