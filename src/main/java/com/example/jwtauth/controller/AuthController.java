package com.example.jwtauth.controller;

import com.example.jwtauth.dto.AuthRequest;
import com.example.jwtauth.dto.AuthResponse;
import com.example.jwtauth.dto.RegisterRequest;
import com.example.jwtauth.model.Role;
import com.example.jwtauth.repo.UserRepository;
import com.example.jwtauth.security.JWTUtils;
import com.example.jwtauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest req) {
        authService.register(req, Role.USER);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/mod")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> registerMod(@Valid @RequestBody RegisterRequest req) {
        authService.register(req, Role.MODERATOR);
        return ResponseEntity.ok("Moderator registered successfully");
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest req) {
        authService.register(req, Role.SUPER_ADMIN);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.authenticate(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken) {
        String username = jwtUtils.extractUsername(refreshToken);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid refresh token"
                ));

        String newAccess = jwtUtils.generateToken(user);
        String newRefresh = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        return ResponseEntity.ok(new AuthResponse(newAccess, newRefresh, "Bearer"));
    }
}