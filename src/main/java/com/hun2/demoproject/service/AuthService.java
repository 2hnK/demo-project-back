package com.hun2.demoproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hun2.demoproject.config.jwt.JwtTokenProvider;
import com.hun2.demoproject.domain.User;
import com.hun2.demoproject.dto.auth.LoginRequestDto;
import com.hun2.demoproject.dto.auth.TokenResponseDto;
import com.hun2.demoproject.dto.auth.SignupRequestDto;
import com.hun2.demoproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponseDto login(LoginRequestDto req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String access = jwtTokenProvider.generateToken(user.getUsername());
        String refresh = jwtTokenProvider.generateRefreshToken(user.getUsername());
        return new TokenResponseDto(access, refresh);
    }

    public TokenResponseDto signup(SignupRequestDto req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .build();
        userRepository.save(user);

        String access = jwtTokenProvider.generateToken(user.getUsername());
        String refresh = jwtTokenProvider.generateRefreshToken(user.getUsername());
        return new TokenResponseDto(access, refresh);
    }

    public TokenResponseDto refresh(String refreshToken) {
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtTokenProvider.extractUsername(refreshToken);
        String newAccess = jwtTokenProvider.generateToken(username);
        return new TokenResponseDto(newAccess, refreshToken);
    }

    // TODO: 로그아웃
    public void logout() {
        return;
    }
}
