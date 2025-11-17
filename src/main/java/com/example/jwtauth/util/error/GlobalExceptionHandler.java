package com.example.jwtauth.util.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {
        log.warn("Validation failed for request {} {}", request.getMethod(), request.getRequestURI());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation error");
        body.put("message", "Request validation failed");
        body.put("path", request.getRequestURI());
        body.put("fieldErrors", fieldErrors);
        return body;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleBadCredentials(BadCredentialsException ex,
                                                    HttpServletRequest request) {
        log.warn("Bad credentials for {} {}", request.getMethod(), request.getRequestURI());

        return errorBody(HttpStatus.UNAUTHORIZED, "Invalid username or password", request.getRequestURI());
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public Map<String, Object> handleLocked(LockedException ex,
                                            HttpServletRequest request) {
        log.warn("Locked account access attempt on {} {}", request.getMethod(), request.getRequestURI());

        return errorBody(HttpStatus.LOCKED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUserNotFound(UsernameNotFoundException ex,
                                                  HttpServletRequest request) {
        log.warn("Username not found on {} {}", request.getMethod(), request.getRequestURI());

        return errorBody(HttpStatus.UNAUTHORIZED, "User not found", request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDenied(AccessDeniedException ex,
                                                  HttpServletRequest request) {
        log.warn("Access denied for {} {}", request.getMethod(), request.getRequestURI());

        return errorBody(HttpStatus.FORBIDDEN, "Access denied", request.getRequestURI());
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleOAuth2Auth(OAuth2AuthenticationException ex,
                                                HttpServletRequest request) {
        log.warn("OAuth2 authentication error on {} {}: {}", request.getMethod(),
                request.getRequestURI(), ex.getError().getDescription());

        return errorBody(HttpStatus.UNAUTHORIZED, "OAuth2 authentication failed", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleAll(Exception ex,
                                         HttpServletRequest request) {
        log.error("Unexpected error on {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        return errorBody(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    private Map<String, Object> errorBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}