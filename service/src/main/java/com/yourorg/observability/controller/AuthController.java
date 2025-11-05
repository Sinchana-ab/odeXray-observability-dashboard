package com.yourorg.observability.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yourorg.observability.dto.LoginRequest;
import com.yourorg.observability.dto.RegisterRequest;
import com.yourorg.observability.model.User;
import com.yourorg.observability.service.AuthService;
import com.yourorg.observability.service.SessionService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;

    public AuthController(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            User u = authService.register(req.getUsername(), req.getPassword());
            return ResponseEntity.ok().body("registered");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            String token = authService.login(req.getUsername(), req.getPassword());
            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/validate-session")
    public ResponseEntity<?> validate(@RequestParam("token") String token) {
        boolean ok = sessionService.validate(token);
        return ResponseEntity.ok().body(Map.of("valid", ok));
    }
}