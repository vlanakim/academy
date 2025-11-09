package com.example.company.controller;

import com.example.company.model.Department;
import com.example.company.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public List<Department> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department create(@RequestBody @Valid Department body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody @Valid Department body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}