package com.yourorg.observability.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private static class SessionEntry {
        Long userId;
        Instant expiry;
        SessionEntry(Long userId, Instant expiry) { this.userId = userId; this.expiry = expiry; }
    }

    private final Map<String, SessionEntry> sessions = new ConcurrentHashMap<>();

    @Value("${app.session.expiry-minutes:30}")
    private int expiryMinutes;

    public String createForUser(Long userId) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES);
        sessions.put(token, new SessionEntry(userId, expiry));
        return token;
    }

    public boolean validate(String token) {
        if (token == null) return false;
        SessionEntry e = sessions.get(token);
        if (e == null) return false;
        if (e.expiry.isBefore(Instant.now())) {
            sessions.remove(token);
            return false;
        }
        return true;
    }

    public Long getUserId(String token) {
        SessionEntry e = sessions.get(token);
        return e == null ? null : e.userId;
    }

    public void invalidate(String token) {
        sessions.remove(token);
    }
}