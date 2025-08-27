package com.hun2.demoproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hun2.demoproject.domain.User;
import com.hun2.demoproject.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 유저 조회
    public User getUser(String userId) {
        return null;
    }

    // TODO: 회원 탈퇴
    public void deleteUser(String userId) {
        return;
    }
}
