package com.example.company;

import com.example.company.controller.EmployeeController;
import com.example.company.model.Department;
import com.example.company.model.Employee;
import com.example.company.service.EmployeeService;
import com.example.company.util.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
@Import(GlobalExceptionHandler.class)
public class EmployeeControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    EmployeeService service;

    @Test
    void create_valid_returns201_and_body() throws Exception {
        Employee input = Employee.builder()
                .firstName("Alice").lastName("Brown")
                .position("Developer").salary(3000.0)
                .department(Department.builder().id(10L).build())
                .build();

        Employee saved = Employee.builder()
                .id(1L).firstName("Alice").lastName("Brown")
                .position("Developer").salary(3000.0)
                .department(Department.builder().id(10L).name("Engineering").build())
                .build();

        when(service.create(any(Employee.class), eq(10L))).thenReturn(saved);

        mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.department.id").value(10));
    }

    @Test
    void create_invalid_returns400_from_bean_validation() throws Exception {
        String badJson = """
        {
          "firstName": "   ",
          "lastName": "Brown",
          "position": "Developer",
          "salary": 3000.0,
          "department": { "id": 10 }
        }
        """;

        mvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("firstName")));
    }
}