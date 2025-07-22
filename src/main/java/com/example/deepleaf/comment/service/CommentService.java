package com.example.deepleaf.comment.service;

import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;
import com.example.deepleaf.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentResponse commentCreate(Long memberId, Long questionId, CommentRequest commentRequest) {
        return null;
    }

    public Page<CommentResponse> getCommentList(Long questionId, Pageable pageable) {

        return null;
    }

    public void deleteComment(Long memberId, Long commentId) {

    }

}
