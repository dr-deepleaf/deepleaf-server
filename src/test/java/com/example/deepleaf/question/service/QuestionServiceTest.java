package com.example.deepleaf.question.service;

import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.dto.request.QuestionCreateRequest;
import com.example.deepleaf.question.dto.request.QuestionUpdateRequest;
import com.example.deepleaf.question.dto.response.QuestionCreateResponse;
import com.example.deepleaf.question.dto.response.QuestionResponse;
import com.example.deepleaf.question.exception.QuestionAccessUnauthorized;
import com.example.deepleaf.question.fixture.QuestionTestFixture;
import com.example.deepleaf.question.repository.QuestionRepository;
import com.example.deepleaf.storage.service.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks QuestionService questionService;

    @Mock
    StorageService storageService;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    void 질문을_생성한다() {
        // given
        Member mockMember = new Member();
        Long memberId = mockMember.getId();
        QuestionCreateRequest request = QuestionTestFixture.createValidQuestionRequest();
        String uploadedImageUrl = "uploaded-image-url";

        when(storageService.uploadFile(request.getImage(), "questionImage", memberId)).thenReturn(uploadedImageUrl);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
        when(questionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        QuestionCreateResponse response = questionService.createQuestion(memberId, request);

        // then
        assertThat(response.getTitle()).isEqualTo("title");
        assertThat(response.getImage_url()).isEqualTo(uploadedImageUrl);
    }

    @Test
    void 질문을_수정한다() {
        // given
        Member member = MemberTestFixture.mockMember();
        Long memberId = member.getId();

        Long questionId = 123L;
        Question originalQuestion = QuestionTestFixture.createMockQuestion();

        originalQuestion.setMember(member); // 작성자 설정

        QuestionUpdateRequest updateRequest = QuestionTestFixture.createQuestionUpdateRequest();
        updateRequest.setImage(QuestionTestFixture.createMockImage());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(originalQuestion));
        when(storageService.uploadFile(any(), any(), any())).thenReturn("new-image-url");

        // when
        QuestionResponse questionResponse = questionService.modifyQuestion(memberId, questionId, updateRequest);

        // then
        assertThat(questionResponse.getTitle()).isEqualTo("update-title");
        assertThat(questionResponse.getImage_url()).isEqualTo("new-image-url");
    }

    @Test
    void 질문을_조회한다(){
        //Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        Question mockQuestion = QuestionTestFixture.createMockQuestion();
        List<Question> responses = new ArrayList<>(10);

        for(int i=0; i < 10; i++){
            responses.add(mockQuestion);
        }

        Page<Question> page = new PageImpl<>(responses, PageRequest.of(0, 10), 100);

        when(questionRepository.findAll(pageRequest)).thenReturn(page);

        //when
        Page<QuestionResponse> questionResponses = questionService.questionList(pageRequest);

        //then
        Assertions.assertThat(questionResponses.getSize()).isSameAs(10);

    }

    @Test
    void 질문을_삭제한다() {
        // given
        Member member = MemberTestFixture.mockMember();
        Long memberId = member.getId(); // 1L
        Long questionId = 123L;
        Question question = QuestionTestFixture.createMockQuestion();
        question.setMember(member);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // when
        questionService.deleteQuestion(memberId, questionId);

        // then
        verify(questionRepository).deleteById(questionId);
    }

    @Test
    void 권한이_없는_사용자는_질문_수정에_실패한다() {
        // given
        Long requesterId = 1L;
        Long questionId = 123L;
        Question question = QuestionTestFixture.createMockQuestion();

        Member author = QuestionTestFixture.accessCheckMockMember();
        question.setMember(author);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // when & then
        assertThatThrownBy(() ->
                questionService.modifyQuestion(requesterId, questionId, QuestionTestFixture.createQuestionUpdateRequest())
        ).isInstanceOf(QuestionAccessUnauthorized.class);
    }

}
