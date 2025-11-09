package com.example.company.service;

import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import com.example.company.repository.DepartmentRepository;
import com.example.company.repository.EmployeeRepository;
import com.example.company.util.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;
    private final DepartmentRepository deptRepo;

    public EmployeeService(EmployeeRepository repo, DepartmentRepository deptRepo) {
        this.repo = repo;
        this.deptRepo = deptRepo;
    }

    public Employee create(@Valid Employee employee, Long departmentId) {
        if (employee.getFirstName() == null || employee.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (employee.getLastName() == null || employee.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (employee.getPosition() == null || employee.getPosition().isBlank()) {
            throw new IllegalArgumentException("Position is required.");
        }
        if (employee.getSalary() == null || employee.getSalary() < 0) {
            throw new IllegalArgumentException("Salary must be non-negative.");
        }

        Department department = deptRepo.findById(departmentId)
                .orElseThrow(() -> new NotFoundException("Department %d not found".formatted(departmentId)));

        employee.setDepartment(department);
        return repo.save(employee);
    }

    public Employee findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee %d not found".formatted(id)));
    }

    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Employee update(Long id, Employee updated, Long departmentId) {
        return repo.findById(id)
                .map(existing -> {
                    if (updated.getFirstName() != null && !updated.getFirstName().isBlank()) {
                        existing.setFirstName(updated.getFirstName());
                    }
                    if (updated.getLastName() != null && !updated.getLastName().isBlank()) {
                        existing.setLastName(updated.getLastName());
                    }
                    if (updated.getPosition() != null && !updated.getPosition().isBlank()) {
                        existing.setPosition(updated.getPosition());
                    }
                    if (updated.getSalary() != null && updated.getSalary() >= 0) {
                        existing.setSalary(updated.getSalary());
                    }

                    if (departmentId != null) {
                        Department newDept = deptRepo.findById(departmentId)
                                .orElseThrow(() -> new NotFoundException("Department %d not found".formatted(departmentId)));
                        existing.setDepartment(newDept);
                    }

                    return repo.save(existing);
                })
                .orElseThrow(() -> new NotFoundException("Employee %d not found".formatted(id)));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Employee %d not found".formatted(id));
        }
        repo.deleteById(id);
    }

    public List<EmployeeProjection> findAllProjected() {
        return repo.findAllBy();
    }

    public List<EmployeeProjection> findByDepartmentName(String name) {
        return repo.findByDepartment_NameIgnoreCase(name);
    }

    public List<EmployeeProjection> findByPosition(String position) {
        return repo.findByPositionIgnoreCase(position);
    }
}