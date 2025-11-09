package com.example.company.projection;

public interface EmployeeProjection {
    String getFirstName();
    String getLastName();
    String getPosition();
    DepartmentView getDepartment();

    default String getFullName() {
        return (getFirstName() + " " + getLastName()).trim();
    }

    default String getDepartmentName() {
        return getDepartment() != null ? getDepartment().getName() : null;
    }

    interface DepartmentView {
        String getName();
    }
}