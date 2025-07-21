package com.example.deepleaf.question.controller;

import com.example.deepleaf.helper.MockBeanInjection;
import com.example.deepleaf.question.contoller.QuestionController;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.fixture.QuestionTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionControllerWebMvcTest extends MockBeanInjection {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final Long memberId = 1L;
    private final String token = "Bearer validToken";
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void 질문_생성_요청() throws Exception {
        //given
        MockMultipartFile mockImage = QuestionTestFixture.createMockImage();
        QuestionCreateRequest request = QuestionTestFixture.createValidQuestionRequest();
        QuestionCreateResponse response = QuestionTestFixture.createValidQuestionResponse();

        //when
        when(questionService.createQuestion(any(), any(QuestionCreateRequest.class)))
                .thenReturn(response);


        //then
        mockMvc.perform(multipart("/api/question/create")
                        .file(mockImage)
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .param("point", String.valueOf(request.getPoint()))
                        .header("AUTHORIZATION", "Bearer validToken") // assuming this is picked up by JWTLoginFilter
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.content").value(response.getContent()));
    }

    @Test
    void 질문_n개씩_조회_요청() throws Exception {
        // given
        QuestionResponse questionResponse = QuestionTestFixture.createQuestionResponse();
        List<QuestionResponse> responses = new ArrayList<>(20);

        for(int i=0; i < 20; i++){
            responses.add(questionResponse);
        }

        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<QuestionResponse> page = new PageImpl<>(responses, PageRequest.of(0, 20), 100);
        when(questionService.questionList(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/api/question")
                        .param("page", "0")
                        .param("size", "20")
                        .header("AUTHORIZATION", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(20));
    }


    @Test
    void 질문_제목_수정() throws Exception {
        QuestionUpdateRequest questionUpdateRequest = QuestionTestFixture.createQuestionUpdateRequest();
        QuestionResponse questionUpdateResponse = QuestionTestFixture.createQuestionUpdateResponse();

        when(questionService.modifyQuestion(any(Long.class),eq(1L), any(QuestionUpdateRequest.class)))
                .thenReturn(questionUpdateResponse);

        mockMvc.perform(multipart("/api/question/{question_id}", 1L)
                        .param("title", questionUpdateRequest.getTitle())
                        .header("AUTHORIZATION", token)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(questionUpdateResponse.getTitle()))
                .andExpect(jsonPath("$.point").value(questionUpdateResponse.getPoint()));
    }


    @Test
    void 질문_id로_삭제() throws Exception {
        doNothing().when(questionService).deleteQuestion(memberId, 1L);

        mockMvc.perform(delete("/api/question/1")
                .header("AUTHORIZATION", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Success"));
    }

}
