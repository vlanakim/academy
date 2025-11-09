package com.example.company;

import com.example.company.controller.DepartmentController;
import com.example.company.model.Department;
import com.example.company.service.DepartmentService;
import com.example.company.util.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartmentController.class)
@Import(GlobalExceptionHandler.class)
public class DepartmentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    DepartmentService service;

    @Test
    void create_valid_returns201_and_body() throws Exception {
        Department input = Department.builder().name("Engineering").build();
        Department saved = Department.builder().id(1L).name("Engineering").build();

        when(service.create(any(Department.class))).thenReturn(saved);

        mvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }
}