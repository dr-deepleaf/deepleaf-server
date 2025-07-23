package com.example.deepleaf.comment.fixture;


import com.example.deepleaf.comment.domain.Comment;
import com.example.deepleaf.comment.dto.request.CommentRequest;
import com.example.deepleaf.comment.dto.response.CommentResponse;

import java.time.LocalDateTime;

public class CommentTestFixture {

    public static CommentRequest commentRequest(){
        return new CommentRequest("comment");
    }

    public static CommentResponse commentResponse(){
        return CommentResponse
                .builder()
                .commentId(1L)
                .commentAuthorId(1L)
                .content("comment")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Comment createComment(CommentRequest commentRequest){
        return Comment.creatWith(commentRequest);
    }
}
