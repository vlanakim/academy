package com.example.company.service;

import com.example.company.model.Department;
import com.example.company.repository.DepartmentRepository;
import com.example.company.util.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repo;

    public DepartmentService(DepartmentRepository repo) {
        this.repo = repo;
    }

    public Department create(@Valid Department d) {
        if (d.getName() == null || d.getName().isBlank()) {
            throw new IllegalArgumentException("Department name is required.");
        }
        return repo.save(d);
    }

    public Department findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Department %d not found".formatted(id)));
    }

    public List<Department> findAll() {
        return repo.findAll();
    }

    public Department update(Long id, @Valid Department patch) {
        return repo.findById(id)
                .map(existing -> {
                    if (patch.getName() != null && !patch.getName().isBlank()) {
                        existing.setName(patch.getName());
                    }
                    return repo.save(existing);
                })
                .orElseThrow(() -> new NotFoundException("Department %d not found".formatted(id)));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Department %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}