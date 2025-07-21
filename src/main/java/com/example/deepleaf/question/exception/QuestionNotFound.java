package com.example.deepleaf.question.exception;

import com.example.deepleaf.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class QuestionNotFound extends GlobalException {
    public QuestionNotFound() {
        super(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND", "존재하지 않는 질문 게시글 입니다.");
    }
}
