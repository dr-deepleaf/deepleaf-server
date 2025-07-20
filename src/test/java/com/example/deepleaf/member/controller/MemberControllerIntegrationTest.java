package com.example.deepleaf.member.controller;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.fixture.AuthTestFixture;
import com.example.deepleaf.auth.repository.AuthRepository;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.dto.MemberInfoResDto;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.example.deepleaf.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void id로_회원_조회를_확인한다() throws JsonProcessingException {

        // 회원가입
        MemberCreateReqDto signupDto = AuthTestFixture.memberCreateDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> signupRequest = new HttpEntity<>(objectMapper.writeValueAsString(signupDto), headers);
        testRestTemplate.postForEntity("/auth/create", signupRequest, MemberCreateResDto.class);


        //로그인
        MemberLoginReqDto memberLoginReqDto = AuthTestFixture.memberLoginDto();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(memberLoginReqDto), headers);
        ResponseEntity<MemberLoginResDto> response = testRestTemplate.exchange("/auth/login", HttpMethod.POST, request, MemberLoginResDto.class);
        String accessToken = response.getBody().getAccessToken();
        Long id = response.getBody().getId();


        //given

        String url = "/api/member?id=" + id;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("AUTHORIZATION", "Bearer " + accessToken);
        HttpEntity<Object> request1 = new HttpEntity<>(httpHeaders);

        //when
        ResponseEntity<MemberInfoResDto> response1 = testRestTemplate.exchange(url, HttpMethod.GET, request1, MemberInfoResDto.class);

        //then
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberInfoResDto body = response1.getBody();
        assertThat(body.getId()).isEqualTo(id);
        assertThat(body.getEmail()).isEqualTo("auth-email");
    }
}
