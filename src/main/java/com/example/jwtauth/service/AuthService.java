package com.example.jwtauth.service;

import com.example.jwtauth.dto.AuthRequest;
import com.example.jwtauth.dto.AuthResponse;
import com.example.jwtauth.dto.RegisterRequest;
import com.example.jwtauth.model.Role;
import com.example.jwtauth.model.User;
import com.example.jwtauth.repo.UserRepository;
import com.example.jwtauth.security.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final LoginAttemptService loginAttemptService;

    public void register(RegisterRequest req, Role role) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new IllegalArgumentException("Username already exists");
        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .accountNonLocked(true)
                .failedAttempts(0)
                .build();
        userRepository.save(user);
        log.info("Registered user {} with role {}", user.getUsername(), user.getRole());
    }

    public AuthResponse authenticate(AuthRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        loginAttemptService.unlockIfTimeExpired(user);

        if (!user.isAccountNonLocked())
            throw new LockedException("Account locked. Try later or contact admin.");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(), req.getPassword()));
            loginAttemptService.onSuccess(user);
        } catch (AuthenticationException ex) {
            loginAttemptService.onFailure(user);
            log.warn("Failed login for {}: {}", req.getUsername(), ex.getMessage());
            throw ex;
        }

        String access = jwtUtils.generateToken(user);
        String refresh = jwtUtils.generateRefreshToken(Map.of("typ","refresh"), user);
        log.info("Generated JWT for {}", user.getUsername());
        return new AuthResponse(access, refresh, "Bearer");
    }
}