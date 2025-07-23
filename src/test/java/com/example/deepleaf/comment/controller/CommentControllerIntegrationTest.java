package com.example.deepleaf.comment.controller;

import com.example.deepleaf.auth.dto.request.MemberCreateReqDto;
import com.example.deepleaf.auth.dto.request.MemberLoginReqDto;
import com.example.deepleaf.auth.dto.response.MemberCreateResDto;
import com.example.deepleaf.auth.dto.response.MemberLoginResDto;
import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
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
public class CommentControllerIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    String accessToken;
    Long memberId;
    Long questionId;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        // 1. 회원가입
        String email = "comment_user_" + UUID.randomUUID() + "@test.com";
        String password = "pass1234";

        MemberCreateReqDto signupDto = MemberCreateReqDto.builder()
                .email(email)
                .password(password)
                .name("댓글 사용자")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> signupRequest = new HttpEntity<>(
                objectMapper.writeValueAsString(signupDto), headers);

        ResponseEntity<MemberCreateResDto> signupResponse =
                restTemplate.postForEntity("/auth/create", signupRequest, MemberCreateResDto.class);

        assertThat(signupResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 2. 로그인
        MemberLoginReqDto loginDto = MemberLoginReqDto.builder()
                .email(email)
                .password(password)
                .build();

        HttpEntity<String> loginRequest = new HttpEntity<>(
                objectMapper.writeValueAsString(loginDto), headers);

        ResponseEntity<MemberLoginResDto> loginResponse =
                restTemplate.postForEntity("/auth/login", loginRequest, MemberLoginResDto.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();

        this.accessToken = loginResponse.getBody().getAccessToken();
        this.memberId = loginResponse.getBody().getId();

        // 3. 질문 생성
        createQuestionForComment();
    }

    void createQuestionForComment() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", "댓글 테스트 질문");
        body.add("content", "댓글 테스트용 내용입니다");
        body.add("point", 50);

        byte[] fakeImage = "fake-image-bytes".getBytes();
        Resource imageResource = new ByteArrayResource(fakeImage) {
            @Override public String getFilename() { return "comment_test.jpg"; }
        };
        body.add("image", imageResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<QuestionCreateResponse> response =
                restTemplate.postForEntity("/api/question/create", request, QuestionCreateResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        this.questionId = response.getBody().getQuestionId();
        assertThat(this.questionId).isNotNull();
    }

    @Test
    void 댓글_생성_조회_삭제_통합_테스트() throws JsonProcessingException {
        // 1. 댓글 생성
        String commentUrl = "/api/question/" + questionId + "/comment";
        CommentRequest commentRequest = new CommentRequest("이건 통합 테스트용 댓글입니다.");

        HttpHeaders headers = new HttpHeaders();
        log.debug("token: {}", accessToken);
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> createRequest = new HttpEntity<>(
                objectMapper.writeValueAsString(commentRequest), headers);

        ResponseEntity<CommentResponse> createResponse =
                restTemplate.postForEntity(commentUrl, createRequest, CommentResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CommentResponse createdComment = createResponse.getBody();
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getContent()).isEqualTo("이건 통합 테스트용 댓글입니다.");

        Long commentId = createdComment.getCommentId();

        // 2. 댓글 리스트 조회
        String getUrl = "/api/question/" + questionId + "/comments?page=0&size=5";
        ResponseEntity<String> listResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).contains("이건 통합 테스트용 댓글입니다.");

        // 3. 댓글 삭제
        String deleteUrl = "/api/comment/" + commentId;
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResponse.getBody()).contains("comment deleted");
    }
}
