package com.hun2.demoproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hun2.demoproject.domain.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // TODO: 회원가입
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return null;
    }

    // TODO: 로그인
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return null;
    }

    // TODO: 로그아웃
    @PostMapping("/logout")
    public void logout() {
        return;
    }

    // TODO: 새로운 토큰 발급
    
    // TODO: 비밀번호 초기화
}