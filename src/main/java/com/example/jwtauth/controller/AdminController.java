package com.example.jwtauth.controller;

import com.example.jwtauth.model.User;
import com.example.jwtauth.repo.UserRepository;
import com.example.jwtauth.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @PostMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String unlock(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();
        loginAttemptService.adminUnlock(user);
        return "Unlocked " + username;
    }
}