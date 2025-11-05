package com.yourorg.observability.service;

import com.yourorg.observability.model.User;
import com.yourorg.observability.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository,
                       SessionService sessionService,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.encoder = encoder;
    }

    public User register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        String hash = encoder.encode(rawPassword);
        User u = new User(username, hash);
        return userRepository.save(u);
    }

    public String login(String username, String rawPassword) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Bad credentials"));
        if (!encoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("Bad credentials");
        }
        return sessionService.createForUser(u.getId());
    }
}
