package com.example.deepleaf.global.config;

import com.example.deepleaf.auth.jwt.JWTLoginFilter;
import com.example.deepleaf.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthFilterConfig {

    private final JWTUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<JWTLoginFilter> jwtLoginFilter(){
        FilterRegistrationBean<JWTLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTLoginFilter(jwtUtil));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
