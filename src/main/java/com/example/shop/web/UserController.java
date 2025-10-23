package com.example.shop.web;

import com.example.shop.dto.UserUpsertDto;
import com.example.shop.json.Views;
import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping
    @JsonView(Views.UserSummary.class)
    public List<User> findAll() {
        return users.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public User findById(@PathVariable Long id) {
        return users.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User %d not found".formatted(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Views.UserDetails.class)
    public User create(@Valid @RequestBody UserUpsertDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        return users.save(user);
    }

    @PutMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public User update(@PathVariable Long id, @Valid @RequestBody UserUpsertDto dto) {
        User user = users.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User %d not found".formatted(id)));
        user.setName(dto.name());
        user.setEmail(dto.email());
        return users.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!users.existsById(id)) throw new EntityNotFoundException("User %d not found".formatted(id));
        users.deleteById(id);
    }
}