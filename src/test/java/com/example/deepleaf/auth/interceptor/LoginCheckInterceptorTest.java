package com.example.deepleaf.auth.interceptor;

import com.example.deepleaf.auth.AuthenticationContext;
import com.example.deepleaf.auth.jwt.JWTUtil;
import com.example.deepleaf.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoginCheckInterceptorTest {
    private LoginCheckInterceptor loginCheckInterceptor;
    private AuthenticationContext authenticationContext;
    @Mock
    private AuthService authService;
    @Mock
    JWTUtil jwtUtil;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    Object handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationContext = new AuthenticationContext();
        loginCheckInterceptor = new LoginCheckInterceptor(jwtUtil, authenticationContext);
    }

    @Test
    void 인증된_사용자의_이름이_authenticationContext에_저장된다( ) throws Exception {
        //Given
        String validToken = "Bearer valid";
        String username = "test";
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtil.getId("valid")).thenReturn(1L);

        //When
        boolean result = loginCheckInterceptor.preHandle(request, response, handler);

        //Then
        assertTrue(result);
        Assertions.assertThat(authenticationContext.getPrincipal()).isEqualTo(1L);
    }
}
