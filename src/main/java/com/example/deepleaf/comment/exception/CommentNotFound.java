package com.example.deepleaf.comment.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class CommentNotFound extends GlobalException {

    public CommentNotFound() {
        super(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "존재하지 않는 댓글입니다.");
    }
}
