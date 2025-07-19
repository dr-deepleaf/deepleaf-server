package com.example.deepleaf.auth.jwt;

import com.example.deepleaf.member.domain.Role;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// Spring Security 구현에서 JWT TokenProvider 역할 -> 토큰 생성 및 토큰 정보 제공
// JWT 0.12.3
@Component
public class JWTUtil {
    private SecretKey secretKey;
    private final long accessTokenValidity = 1000 * 60 * 60; // 시간
    //private final long refreshTokenValidity = 1000 * 60 * 60; // 1시간

    // 인코딩된 문자열 비밀키로 비밀키 객체 생성 -> JWT 서명/검증에 사용됨
    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // utils
    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("member_id", Long.class);
    }
    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Role getRole(String token) {
        String roleString = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        return Role.valueOf(roleString);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }


    // 토큰 생성
    public String createAccessToken(Long member_id, String email, Role role) {
        return Jwts.builder()
                .claim("member_id", member_id)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }
}
