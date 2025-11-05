package com.yourorg.observability.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourorg.observability.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
