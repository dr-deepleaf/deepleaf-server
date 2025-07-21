package com.example.deepleaf.question.fixture;

import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import org.springframework.mock.web.MockMultipartFile;

public class QuestionTestFixture {
    public static QuestionCreateRequest createValidQuestionRequest() {
        return QuestionCreateRequest.builder()
                .title("title")
                .content("content")
                .image(createMockImage())
                .point(100L)
                .build();
    }

    public static QuestionCreateResponse createValidQuestionResponse() {
        return QuestionCreateResponse.builder()
                .questionId(1L)
                .title("title")
                .content("content")
                .image_url("image_url")
                .point(100L)
                .build();
    }

    public static Question createMockQuestion(){
        return Question.createQuestion(QuestionTestFixture.createValidQuestionRequest(), "image_irl");
    }

    public static QuestionResponse createQuestionResponse(){
        return QuestionResponse.create(createMockQuestion());
    }

    public static QuestionUpdateRequest createQuestionUpdateRequest() {
        return QuestionUpdateRequest.builder()
                .title("update-title")
                .build();
    }

    public static QuestionResponse createQuestionUpdateResponse() {
        QuestionResponse questionResponse = createQuestionResponse();
        questionResponse.setTitle("update-title");

        return questionResponse;
    }

    public static MockMultipartFile createMockImage() {
        return new MockMultipartFile(
                "image",                          // 필드 이름
                "image.jpg",                      // 파일 이름
                "image/jpeg",                     // MIME 타입
                "fake image content".getBytes()   // 파일 내용 (byte[])
        );
    }
}
