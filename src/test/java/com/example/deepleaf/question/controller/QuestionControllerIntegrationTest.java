package com.example.deepleaf.question.controller;


import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.repository.QuestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    protected String accessToken;
    protected Long memberId;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        // 고유한 이메일 생성
        String uniqueEmail = "test_" + UUID.randomUUID() + "@example.com";
        String password = "securePassword123";
        String name = "통합테스트사용자";

        // 1. 회원가입 요청
        MemberCreateReqDto signupDto = MemberCreateReqDto.builder()
                .email(uniqueEmail)
                .password(password)
                .name(name)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> signupRequest = new HttpEntity<>(
                objectMapper.writeValueAsString(signupDto), headers);

        ResponseEntity<MemberCreateResDto> signupResponse =
                restTemplate.postForEntity("/auth/create", signupRequest, MemberCreateResDto.class);

        if (signupResponse.getStatusCode() != HttpStatus.CREATED) {
            throw new RuntimeException("회원가입 실패");
        }

        // 2. 로그인 요청
        MemberLoginReqDto loginDto = MemberLoginReqDto.builder()
                .email(uniqueEmail)
                .password(password)
                .build();

        HttpEntity<String> loginRequest = new HttpEntity<>(
                objectMapper.writeValueAsString(loginDto), headers);

        ResponseEntity<MemberLoginResDto> loginResponse =
                restTemplate.postForEntity("/auth/login", loginRequest, MemberLoginResDto.class);

        if (loginResponse.getStatusCode() != HttpStatus.OK || loginResponse.getBody() == null) {
            throw new RuntimeException("로그인 실패");
        }

        // 3. 토큰과 memberId 저장
        this.accessToken = loginResponse.getBody().getAccessToken();
        this.memberId = loginResponse.getBody().getId();

        log.debug("token: {}", accessToken);
    }

    @Test
    void 질문_생성_api_테스트(){

        //given
        String url = "/api/question/create";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", "통합 테스트용 제목");
        body.add("content", "통합 테스트용 내용");
        body.add("point", 100);

        // 가짜 이미지 파일 생성
        byte[] imageBytes = "fake image".getBytes();
        Resource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "test.jpg";
            }
        };
        body.add("image", imageResource);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(accessToken);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<QuestionCreateResponse> createResponse = restTemplate.postForEntity(url, request, QuestionCreateResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long questionId = createResponse.getBody().getQuestionId();
        assertThat(questionId).isNotNull();

        // ======== 2. 질문 리스트 조회 ========
        ResponseEntity<String> listResponse = restTemplate.exchange(
                "/api/question?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(makeAuthHeader()),
                String.class
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).contains("통합 테스트용 제목");

        // ======== 3. 질문 수정 ========
        MultiValueMap<String, Object> updateBody = new LinkedMultiValueMap<>();
        updateBody.add("title", "수정된 제목");
        updateBody.add("content", "수정된 내용");

        HttpEntity<MultiValueMap<String, Object>> updateRequest = new HttpEntity<>(updateBody, headers);
        ResponseEntity<QuestionResponse> updateResponse = restTemplate.exchange(
                "/api/question/" + questionId,
                HttpMethod.PUT,
                updateRequest,
                QuestionResponse.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getTitle()).isEqualTo("수정된 제목");

        // ======== 4. 질문 삭제 ========
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/question/" + questionId,
                HttpMethod.DELETE,
                new HttpEntity<>(makeAuthHeader()),
                String.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResponse.getBody()).contains("Deleted Success");
    }

    private HttpHeaders makeAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }

}
