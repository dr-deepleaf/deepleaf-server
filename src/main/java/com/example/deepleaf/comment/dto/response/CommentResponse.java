package com.example.deepleaf.comment.dto.response;

import com.example.deepleaf.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private Long commentAuthorId;
    private String content;
    private LocalDateTime createdAt;

    private CommentResponse(Comment comment, Long memberId){
        this.commentId = comment.getId();
        this.commentAuthorId = memberId;
        this.content = comment.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public static CommentResponse createWith(Comment saved, Long memberId) {
        return new CommentResponse(saved, memberId);
    }
    public static CommentResponse createWith(Comment saved) {
        Long memberId = saved.getMember().getId();
        return new CommentResponse(saved, memberId);
    }

}
