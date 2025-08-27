package com.hun2.demoproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hun2.demoproject.domain.User;
import com.hun2.demoproject.repository.UserRepository;

@Service
public class AuthService {

    // TODO: 회원가입
    public User signup(User user) {
        return null;
    }

    // TODO: 로그인
    public User login(User user) {
        return null;
    }

    // TODO: 로그아웃
    public void logout() {
        return;
    }
}
