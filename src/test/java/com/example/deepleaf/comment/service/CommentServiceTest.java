package com.example.deepleaf.comment.service;


import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.comment.exception.CommentAccessUnauthorized;
import com.example.deepleaf.comment.fixture.CommentTestFixture;
import com.example.deepleaf.comment.repository.CommentRepository;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.fixture.MemberTestFixture;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.fixture.QuestionTestFixture;
import com.example.deepleaf.question.repository.QuestionRepository;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    void 댓글_생성_연관관계_확인(){
        //given
        Member member = MemberTestFixture.mockMember();
        Question mockQuestion = QuestionTestFixture.createMockQuestion();
        Long memberId = member.getId();
        Long questionId = mockQuestion.getId();
        CommentRequest commentRequest = CommentTestFixture.commentRequest();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(mockQuestion));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        CommentResponse response = commentService.commentCreate(memberId, questionId, commentRequest);

        //then
        Assertions.assertThat(response.getCommentAuthorId()).isEqualTo(memberId);
    }


    @Test
    void 댓글_리스트_5개_조회(){
        //given
        Long questionId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Comment> commentList = new ArrayList<>(5);
        Page<Comment> pages = new PageImpl<>(commentList,pageRequest,100);

        when(commentRepository.findByQuestionId(questionId,pageRequest)).thenReturn(pages);

        //when
        Page<CommentResponse> result = commentService.getCommentList(questionId, pageRequest);

        //then
        Assertions.assertThat(result.getSize()).isSameAs(5);

    }

    @Test
    void 권한이_없는_사용자는_댓글_수정에_실패한다() {
        // given
        Long otherUserId = 2L; // 삭제 요청자
        Long commentId = 100L;

        Member owner = MemberTestFixture.mockMember();
        Comment comment = CommentTestFixture.createComment(CommentTestFixture.commentRequest());
        comment.setMember(owner);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(otherUserId, commentId))
                .isInstanceOf(CommentAccessUnauthorized.class);
    }


}