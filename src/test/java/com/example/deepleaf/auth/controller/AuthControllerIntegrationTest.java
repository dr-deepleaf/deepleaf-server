package com.example.deepleaf.auth.controller;


import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.auth.fixture.AuthTestFixture;
import com.example.deepleaf.auth.repository.AuthRepository;
import com.example.deepleaf.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private AuthRepository authRepository;

    @BeforeEach
    public void setUp() {
        authRepository.deleteAll();
    }

    @Test
    void 회원가입_성공을_확인한다() throws Exception {

        // given
        MemberCreateReqDto memberCreateReqDto = AuthTestFixture.memberCreateDto();
        String url = "/auth/create"; // 포트 제거됨

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(memberCreateReqDto), headers);

        // when
        ResponseEntity<MemberCreateResDto> response =
                testRestTemplate.exchange(url, HttpMethod.POST, request, MemberCreateResDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MemberCreateResDto body = response.getBody();
        assertThat(body).isNotNull();

    }

    @Test
    void 로그인_성공을_확인한다() throws Exception{

        // given
        // 회원가입 먼저 수행
        MemberCreateReqDto signupDto = AuthTestFixture.memberCreateDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> signupRequest = new HttpEntity<>(objectMapper.writeValueAsString(signupDto), headers);
        testRestTemplate.postForEntity("/auth/create", signupRequest, MemberCreateResDto.class);

        MemberLoginReqDto memberLoginReqDto = AuthTestFixture.memberLoginDto();

        String url = "/auth/login";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(memberLoginReqDto), headers);

        // When
        ResponseEntity<MemberLoginResDto> response = testRestTemplate.exchange(url, HttpMethod.POST, request, MemberLoginResDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberLoginResDto body = response.getBody();
        assertThat(body).isNotNull();

    }
}
