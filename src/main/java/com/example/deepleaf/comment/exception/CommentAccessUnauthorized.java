package com.example.deepleaf.comment.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class CommentAccessUnauthorized extends GlobalException {

    public CommentAccessUnauthorized() {
        super(HttpStatus.FORBIDDEN, "CANNOT_ACCESS_COMMENT", "해당 댓글에 접근 권한이 없습니다.");
    }
}
