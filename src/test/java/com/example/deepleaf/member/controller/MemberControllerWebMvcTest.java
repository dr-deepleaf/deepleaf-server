package com.example.deepleaf.member.controller;

import com.example.deepleaf.helper.MockBeanInjection;
import com.example.deepleaf.member.dto.MemberInfoResDto;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MemberController.class)
public class MemberControllerWebMvcTest extends MockBeanInjection {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = "Bearer validToken";
    @Test
    void 회원ID로_회원정보를_조회한다 () throws Exception {
        //given
        Long memberId = 1L;
        MemberInfoResDto memberInfoResDto = MemberTestFixture.memberInfoResDto();

        //when
        when(memberService.getMemberInfo(memberId)).thenReturn(memberInfoResDto);

        //then
        mockMvc.perform(get("/api/member")
                .param("id", String.valueOf(memberId))
                .header(AUTHORIZATION, token))
                .andExpect(jsonPath("$.id").value(memberId));

    }
}
