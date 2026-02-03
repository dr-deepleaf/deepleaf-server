package com.example.deepleaf.comment.controller;

import com.example.deepleaf.auth.annotation.LoginMember;
import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/question/{question_id}/comment")
    public ResponseEntity<CommentResponse> createComment(
            @LoginMember Long memberId,
            @PathVariable(value = "question_id") Long questionId,
            @RequestBody CommentRequest commentRequest
    ) {
        
        CommentResponse response = commentService.commentCreate(memberId, questionId, commentRequest);
        return ResponseEntity.status(201).body(response);
    }

    //댓글 불러오기
    @GetMapping("/question/{question_id}/comments")
    public ResponseEntity<Page<CommentResponse>> getCommentList(
            @PathVariable(value = "question_id") Long questionId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> responses = commentService.getCommentList(questionId, pageable);
        return ResponseEntity.ok().body(responses);
    }


    // 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(
            @LoginMember Long memberId,
            @PathVariable(value = "comment_id") Long commentId){
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().body("comment deleted");
    }


}
