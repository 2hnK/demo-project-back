package com.hun2.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hun2.demoproject.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
