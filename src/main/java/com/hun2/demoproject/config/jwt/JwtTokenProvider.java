package com.hun2.demoproject.config.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.access-expiration}")
    private long jwtAccessExpiration;

    @Value("${jwt.token.refresh-expiration}")
    private long jwtRefreshExpiration;

    // BASE64 문자열로 된 비밀키를 디코딩하여 키 객체로 변환
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 기본 토큰 생성
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    // 추가 클레임을 포함한 토큰 생성
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, jwtAccessExpiration);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, jwtRefreshExpiration);
    }

    // 토큰 빌드 메서드
    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // 토큰에서 만료 시간 추출
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    // 특정 클레임 추출
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 유효성 검증
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰 만료 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰에서 모든 클레임 가져오기
    public Claims getAllClaimsFromToken(String token) {
        return extractAllClaims(token);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰 타입 확인
    public boolean isRefreshToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            long timeUntilExpiration = expiration.getTime() - now.getTime();

            return timeUntilExpiration > jwtAccessExpiration;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}