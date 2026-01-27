package com.example.deepleaf.auth.jwt;

import com.example.deepleaf.auth.exception.InvalidToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Security 체인 상의 필터 역할과 똑같음.
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTLoginFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWT Login Filter Working...");
        String requestURI = request.getRequestURI();

        // 로그인 필터를 적용하지 않을 경로
        if(requestURI.startsWith("/auth/") || requestURI.startsWith("/health")){
            filterChain.doFilter(request,response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        log.debug("authorization: {}", authorization);
        // 요청 헤더 인증 정보 검사
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new InvalidToken();
        }


        String token = authorization.split(" ")[1];
        // 토큰 만료 검사
        if(jwtUtil.isExpired(token)){
            throw new InvalidToken();
        }

        // 아직은 엑세스 토큰 저장하지 않음. 후에 구현
        filterChain.doFilter(request,response);

    }
}
