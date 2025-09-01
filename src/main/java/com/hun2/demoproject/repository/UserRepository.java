package com.hun2.demoproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hun2.demoproject.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
}
