package com.example.jwtauth.service;

import com.example.jwtauth.model.User;
import com.example.jwtauth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final UserRepository userRepository;

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public void onSuccess(User user) {
        if (user.getFailedAttempts() > 0) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            user.setAccountNonLocked(true);
            userRepository.save(user);
        }
    }

    public void onFailure(User user) {
        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);
        if (attempts >= MAX_ATTEMPTS) {
            user.setAccountNonLocked(false);
            user.setLockTime(Instant.now());
            log.warn("User {} locked", user.getUsername());
        }
        userRepository.save(user);
    }

    public void unlockIfTimeExpired(User user) {
        if (!user.isAccountNonLocked() && user.getLockTime() != null) {
            if (Instant.now().isAfter(user.getLockTime().plus(LOCK_DURATION))) {
                user.setAccountNonLocked(true);
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
                log.info("User {} unlocked automatically after duration", user.getUsername());
            }
        }
    }

    public void adminUnlock(User user) {
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
    }
}