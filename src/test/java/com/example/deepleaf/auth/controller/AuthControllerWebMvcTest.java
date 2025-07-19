package com.example.deepleaf.auth.controller;

import com.example.deepleaf.auth.controller.AuthController;
import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.fixture.AuthTestFixture;
import com.example.deepleaf.helper.MockBeanInjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerWebMvcTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입을_한다()throws Exception{
        // given
        MemberCreateReqDto memberCreateReqDto = AuthTestFixture.memberCreateDto();
        MemberCreateResDto memberCreateResDto = new MemberCreateResDto(1L);

        when(authService.create(memberCreateReqDto)).thenReturn(memberCreateResDto);

        // When & Then
        mockMvc.perform(post("/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreateReqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void 로그인을_한다()throws Exception{
        // given
        MemberLoginReqDto memberLoginReqDto = AuthTestFixture.memberLoginDto();
        MemberLoginResDto memberLoginResDto = AuthTestFixture.memberLoginResponseDto();

        when(authService.login(memberLoginReqDto)).thenReturn(memberLoginResDto);

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

}
