package com.hun2.demoproject.config.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

    @Value("${jwt.issuer:demo-project}")
    private String issuer;

    private volatile SecretKey cachedSigningKey; // TODO: volatile 키워드 사용 이유 학습

    // BASE64 문자열로 된 비밀키를 디코딩하여 키 객체로 변환 (1회 캐싱)
    private SecretKey getSigningKey() {
        SecretKey local = cachedSigningKey;
        if (local == null) {
            synchronized (this) {
                local = cachedSigningKey;
                if (local == null) {
                    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
                    cachedSigningKey = local = Keys.hmacShaKeyFor(keyBytes);
                }
            }
        }
        return local; // TODO: cachedSigningKey 무효화 메서드 추가 필요
    }

    // 기본 토큰 생성
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    // 추가 클레임을 포함한 토큰 생성 (access)
    public String generateToken(Map<String, Object> extraClaims, String username) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("token_type", "access");
        return buildToken(claims, username, jwtAccessExpiration);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "refresh");
        return buildToken(claims, username, jwtRefreshExpiration);
    }

    // 토큰 빌드 메서드
    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuer(issuer)
                .id(UUID.randomUUID().toString())
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

    // 토큰 서명 만료 여부 검증
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Access 토큰만 유효성 검사 후 Claims 반환 (유효하지 않으면 null)
    public Claims parseIfValidAccess(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("token_type", String.class);
            if (!"access".equals(tokenType)) {
                return null;
            }
            if (isTokenExpired(token)) {
                return null;
            }
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    // 토큰 타입 확인
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("token_type", String.class);
            return "refresh".equals(tokenType) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}