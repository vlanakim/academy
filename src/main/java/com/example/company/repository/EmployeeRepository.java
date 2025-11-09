package com.example.company.repository;

import com.example.company.model.Employee;
import com.example.company.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<EmployeeProjection> findAllBy();

    List<EmployeeProjection> findByDepartment_NameIgnoreCase(String departmentName);

    List<EmployeeProjection> findByPositionIgnoreCase(String position);
}