package com.example.deepleaf.member.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final int expiration;
    private Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey; // Base64 인코딩된 문자열 형태의 비밀 키 값
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
        // 비밀키 객체 -> JWT 서명/검증에 사용됨
    }

    public String createToken(String email, String role){
        Claims claims = Jwts.claims().setSubject(email); // 토큰에 대한 주체
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder() // 빌더 사용시 헤더는 자동 생성
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ expiration*60*1000L))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }
}
