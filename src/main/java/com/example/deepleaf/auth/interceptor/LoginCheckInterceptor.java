package com.example.deepleaf.auth.interceptor;

import com.example.deepleaf.auth.AuthenticationContext;
import com.example.deepleaf.auth.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final JWTUtil jwtUtil;
    private final AuthenticationContext ac;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("login interceptor work");

        String accessToken = request.getHeader("Authorization").substring(7);

        Long memberId = jwtUtil.getId(accessToken);
        ac.setAuthentication(memberId);
        return true;
    }
}
