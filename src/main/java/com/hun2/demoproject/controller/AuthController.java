package com.hun2.demoproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hun2.demoproject.dto.auth.LoginRequestDto;
import com.hun2.demoproject.dto.auth.SignupRequestDto;
import com.hun2.demoproject.dto.auth.TokenResponseDto;
import com.hun2.demoproject.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto req) {
        return authService.login(req);
    }

    @PostMapping("/signup")
    public TokenResponseDto signup(@RequestBody SignupRequestDto req) {
        return authService.signup(req);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    // TODO: 로그아웃
    @PostMapping("/logout")
    public void logout() {
        return;
    }

}