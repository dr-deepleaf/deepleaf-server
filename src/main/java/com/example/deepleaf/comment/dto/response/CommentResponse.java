package com.example.deepleaf.comment.dto.response;

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
    private Long questionId;
    private Long commentId;
    private Long commentAuthorId;
    private String content;
    private LocalDateTime createdAt;

}
