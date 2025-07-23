package com.example.deepleaf.comment.service;

import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.comment.exception.CommentAccessUnauthorized;
import com.example.deepleaf.comment.exception.CommentNotFound;
import com.example.deepleaf.comment.repository.CommentRepository;
import com.example.deepleaf.member.domain.Member;
import com.example.deepleaf.member.exception.MemberNotFound;
import com.example.deepleaf.member.repository.MemberRepository;
import com.example.deepleaf.question.domain.Question;
import com.example.deepleaf.question.exception.QuestionNotFound;
import com.example.deepleaf.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    public CommentResponse commentCreate(Long memberId, Long questionId, CommentRequest commentRequest) {

        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        Comment comment = Comment.creatWith(commentRequest);
        question.addComment(comment);
        member.addComment(comment);

        Comment saved = commentRepository.save(comment);

        return CommentResponse.createWith(saved, memberId);
    }

    public Page<CommentResponse> getCommentList(Long questionId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByQuestionId(questionId, pageable);
        return comments.map(CommentResponse::createWith);
    }

    public void deleteComment(Long memberId, Long commentId) {
        checkAuthorizedAccess(memberId,commentId);
        commentRepository.deleteById(commentId);
    }

    // 댓글 수정 및 삭제 권한 확인
    public Comment checkAuthorizedAccess(Long memberId, Long commentId) {
        // 1. comment를 조회한다.
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);
        log.debug("comment master: {}", comment.getMember().getId());
        log.debug("request: {}", memberId);
        // 2. 현재 로그인한 사용자가 댓글 수정 권한이 있는 지 체크한다.
        if(!comment.getMember().getId().equals(memberId)) {
            throw new CommentAccessUnauthorized();
        }

        return comment;
    }


}
