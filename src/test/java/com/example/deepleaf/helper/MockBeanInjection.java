package com.example.deepleaf.helper;

import com.example.deepleaf.auth.AuthenticationContext;
import com.example.deepleaf.auth.jwt.JWTUtil;
import com.example.deepleaf.auth.service.AuthService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class MockBeanInjection {
    //auth
    @MockitoBean
    protected JWTUtil jwtUtil;
    @MockitoBean
    protected AuthenticationContext authenticationContext;
    @MockitoBean
    protected AuthService authService;

}
