package com.example.deepleaf.auth.jwt;

import com.example.deepleaf.auth.exception.InvalidToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class JwtLoginFilterTest {
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private FilterChain filterChain;

    private JWTLoginFilter jwtLoginFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtLoginFilter = new JWTLoginFilter(jwtUtil);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }


    @Test
    void auth경로는_JWTLoginFilter를_통과한다 () throws ServletException, IOException {

        //Given
        request.setRequestURI("/auth/login");

        //When
        jwtLoginFilter.doFilter(request,response,filterChain);

        //Then
        verify(filterChain, times(1)).doFilter(request,response);

    }

    @Test
    void api로_시작하는_요청에_인증정보가_없으면_AuthorizationInfoNotExist가_발생한다 () throws ServletException, IOException {

        //Given
        request.setRequestURI("/api/resource");

        //When & Then
        assertThrows(InvalidToken.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청에_authorization헤더_값이_Bearer로_시작하지_않으면_AuthorizationInfoNotExist가_발생한다 () throws ServletException, IOException {
        //Given - Bearer로 시작하지 않은 Authorization 헤더
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bear");

        //When & Then
        assertThrows(InvalidToken.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청의_토큰이_만료되었다면_TokenExpired가_발생한다 () throws ServletException, IOException {
        //Given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer expired");
        when(jwtUtil.isExpired("expired")).thenReturn(true);

        //When & Then
        assertThrows(InvalidToken.class, () -> jwtLoginFilter.doFilter(request,response,filterChain));
    }

    @Test
    void api로_시작하는_요청의_토큰이_유효하다면_JWTLoginFilter를_통과한다 () throws ServletException, IOException {
        // given
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "Bearer validToken");

        //when
        when(jwtUtil.isExpired("validToken")).thenReturn(false);
        jwtLoginFilter.doFilter(request, response, filterChain);

        // 필터 체인이 실행되었는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());

    }
}
